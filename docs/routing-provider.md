# Phase 3 Routing Provider Contract

Phase 3 uses OpenRouteService (ORS) as the first replaceable `RoutingProvider`. Requests use
`POST /v2/directions/driving-hgv/geojson`, and send the confirmed vehicle height, width,
length, gross weight, axle load, hazmat state, and an ORS vehicle type. ORS supports tollway
and ferry avoidance for driving profiles; it does not expose an unpaved-road avoidance flag,
so the application does not send one and shows an explicit warning when that preference was
selected.

References:

- [ORS routing options](https://giscience.github.io/openrouteservice/api-reference/endpoints/directions/routing-options)
- [ORS requests and GeoJSON responses](https://giscience.github.io/openrouteservice/api-reference/endpoints/directions/requests-and-return-types)
- [ORS extra information](https://giscience.github.io/openrouteservice/api-reference/endpoints/directions/extra-info/)

For every accepted route, the app stores the exact request payload (which never contains the
API key), vehicle-profile snapshot, returned geometry, steps, warnings, route summary,
provider attribution and engine dates when returned, response SHA-256, local request ID, and
request/response timestamps. Unknown, corrupt, or invalid persisted-route schemas fail closed.
Changing the vehicle profile deletes the old route because its calculations no longer match.

## Credential boundary

Development builds read `ORS_API_KEY` from a Gradle property or environment variable. The key
is not committed, logged, or included in route provenance, but it is embedded in the APK and
must be assumed extractable. A production build must not ship a privileged shared secret;
provider restrictions, a narrowly scoped proxy, self-hosting, or another reviewed design is
required before distribution.

## Safety boundary

All Phase 3 results remain `Unverified`. They are decision support derived from available map
and restriction data, not proof of legality, clearance, passability, or safety. Phase 3 does
not start guidance. Phase 4 must implement mandatory route overview and driver review before
navigation can begin.
