# Phase 4 Driver Route Review

Phase 4 keeps each newly calculated or legacy saved route in the `UNVERIFIED` state and
blocks route-map access until the driver completes the review. The review shows:

- origin, destination, distance, and estimated duration;
- the exact confirmed vehicle/load profile used for calculation;
- every application warning and the number of restriction segments reported by ORS;
- route steps and provider/request provenance, response hash, and graph date when available;
- `DATA WARNING` and `OFFLINE / STALE` states when applicable.

The driver must separately acknowledge that they compared the route with official
restrictions, permits, and dispatch instructions; verified that the vehicle and load are
current; and understand that signs, officials, permits, closures, weather, and real-world
conditions override the application.

The saved acknowledgment contains the route request ID, vehicle-profile confirmation time,
review time, and acknowledgment-version number. The map gate accepts it only when it matches
the exact route and profile. A changed profile deletes the route; corrupt, legacy, mismatched,
or unknown review records fail closed as unverified. The acknowledgment is explicitly not a
legal, clearance, compliance, or safety certification.

Phase 4 does not implement turn-by-turn guidance. Phase 5 can build the offline corridor only
on this reviewed-route boundary and must preserve visible stale/offline warnings.
