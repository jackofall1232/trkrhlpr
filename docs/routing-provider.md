# Phase 3 Routing Provider Contract

Phase 3 uses OpenRouteService (ORS) as the first replaceable `RoutingProvider`. Requests use
`POST {OrsApi.BASE_URL}/v2/directions/driving-hgv/geojson`, and send the confirmed vehicle
height, width, length, gross weight, axle load, hazmat state, and an ORS vehicle type. ORS supports tollway
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

## API host (`OrsApi.kt`)

HeiGIT deprecated `api.openrouteservice.org` in favour of the unified `api.heigit.org`
gateway ([announcement, 2026-04-28](https://ask.openrouteservice.org/t/deprecating-api-openrouteservice-org-in-favour-of-api-heigit-org/7912)).
Every hosted-ORS URL in the app derives from the single `OrsApi.BASE_URL` constant
(`feature/routing/.../OrsApi.kt`), currently `https://api.heigit.org/ors` with the
gateway's per-service `/ors` prefix.

**Unverified caveat:** the exact path mapping under the new gateway could not be
re-verified from the restricted build environment (the ORS docs, forum, and api.heigit.org
hosts are all blocked there). Verify once with a keyed request before relying on it:

```bash
curl -s -H "Authorization: $ORS_API_KEY" \
  "https://api.heigit.org/ors/geocode/autocomplete?text=Denver&boundary.country=US&size=1"
curl -s -X POST -H "Authorization: $ORS_API_KEY" -H "Content-Type: application/json" \
  -d '{"coordinates":[[-104.99,39.74],[-104.61,39.86]]}' \
  "https://api.heigit.org/ors/v2/directions/driving-hgv/geojson"
```

A GeoJSON body means the paths are right; a 404/405 means the prefix is wrong — fix
`OrsApi.BASE_URL` (one line) to match the example URLs shown in the HeiGIT dashboard.

## Geocoding (address input)

The route planner takes addresses, not raw coordinates. `OrsGeocodingProvider` resolves them
with the same ORS account and key as routing (no second provider or credential):

- `GET /geocode/autocomplete` — search-as-you-type for the origin and destination fields,
  bounded to `boundary.country=US` (first-milestone scope) and at most 6 suggestions. The
  destination search passes the resolved origin as `focus.point.*` to rank nearby results
  higher. Tapping a suggestion is the resolution step: the suggestion's own coordinates feed
  the unchanged coordinate-based `RoutingProvider.calculate` call, so free-typed text that
  was never matched can never route to (0,0) — the calculate action stays disabled until
  both endpoints are resolved.
- `GET /geocode/reverse` — after "Use my current location", the approximate device position
  is reverse-geocoded once so the driver can confirm a human-readable "Near …" label. This
  is confirmation display only: routing always keeps the exact device coordinates, and a
  failed reverse lookup falls back to showing the coordinates without blocking routing.

ORS free-tier geocoding allows roughly 100 requests per minute against a shared daily quota
([plan limits](https://account.heigit.org/info/plans)). Autocomplete therefore fires only
after a 450 ms typing pause, requires at least 3 characters, and cancels the in-flight
request on every new keystroke (`AddressAutocompleteController`), keeping a realistic
address entry to a handful of requests.

Location access reuses the existing opt-in **approximate** (coarse) location boundary — the
manifest still strips `ACCESS_FINE_LOCATION`. Permission is requested only when the driver
taps the location button; denial shows an explicit message and manual address entry remains
available, so the flow never dead-ends.

## Credential boundary

Development builds read `ORS_API_KEY` from, in precedence order: a Gradle property
(`-PORS_API_KEY=…` or `~/.gradle/gradle.properties`), the `ORS_API_KEY` environment
variable, or the gitignored `local.properties` at the repository root
(`ORS_API_KEY=your-key` on its own line). CI reads it from the optional GitHub Actions
repository secret `ORS_API_KEY`. All of these are uncommitted sources — the key must never
be hardcoded in any source or committed file. The key is not committed, logged, or included
in route provenance, but it is embedded in the APK and must be assumed extractable. A production build must not ship a privileged shared secret;
provider restrictions, a narrowly scoped proxy, self-hosting, or another reviewed design is
required before distribution.

## Safety boundary

All Phase 3 results remain `Unverified`. They are decision support derived from available map
and restriction data, not proof of legality, clearance, passability, or safety. Phase 3 does
not start guidance. Phase 4 must implement mandatory route overview and driver review before
navigation can begin.
