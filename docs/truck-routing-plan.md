# Commercial-Truck Routing Implementation Plan

## Product safety contract

Truck routing is decision support, not a guarantee that a route is legal, safe, passable,
or suitable for a particular vehicle or load. Route results depend on the accuracy,
coverage, and freshness of third-party map and restriction data. The driver remains
responsible for checking road signs, official restrictions, permits, dispatch instructions,
weather, clearances, and current conditions, and must never follow the app when real-world
conditions or official instructions conflict with it.

The product must not describe a route as "truck-safe," "guaranteed clear," or
"STAA-compliant." It may say that a route was *calculated using the supplied vehicle
profile and available commercial-vehicle restriction data*. Legal review is required
before release; warning text alone is not assumed to transfer or eliminate liability.

Every route has a visible state:

- **Unverified** — the driver has not completed route review.
- **Driver reviewed** — the driver acknowledged the review workflow; this is not a safety
  certification.
- **Data warning** — inputs, restrictions, or map data are missing, stale, or conflicting.
- **Offline/stale** — live restriction updates and online rerouting are unavailable.

The feature remains outside the first production milestone. Each phase below requires
its acceptance criteria and applicable human safety gate to pass before the next phase.

## Phase 0 — Safety contract and terminology

- Replace guarantee and compliance claims with qualified routing-assistance language.
- Approve route states, driver-review requirements, warnings, and safe failure behavior.
- Define behavior for missing vehicle data, routing failures, lost connectivity, stale
  routes, and conflicts with signs or official instructions.
- Obtain legal review of release wording and risk allocation.

**Exit criteria:** The safety contract, terminology, degraded states, and review gate are
approved and testable.

## Phase 1 — Read-only MapLibre map

- Add a dedicated `feature:routing` module and MapLibre integration for Compose.
- Display a basic map, required attribution, map controls, and optional current position.
- Keep map styles and tile providers behind replaceable interfaces.
- Request Internet and location permissions contextually and handle denial gracefully.
- Select a tile provider only after licensing, privacy, caching, attribution, and offline
  terms are recorded. Standard OpenStreetMap tile servers must not be bulk-prefetched.

**Exit criteria:** The map works on supported phone and tablet configurations, permission
denial is safe, attribution is correct, and the UI makes no routing claim.

## Phase 2 — Vehicle profile

- Capture vehicle/trailer type, total height, width, length, gross weight, axle load, axle
  count, hazmat state, and supported avoidances.
- Store canonical measurements in metric units and support US customary input/display.
- Validate missing and implausible values without silently substituting permissive defaults.
- Require confirmation before route calculation and after equipment or load changes.

**Exit criteria:** A route cannot be calculated from an incomplete, invalid, or
unconfirmed profile, and profile persistence/migration tests pass.

## Phase 3 — Online route preview

- Implement an OpenRouteService client using `driving-hgv` and supported vehicle
  restrictions; keep the routing provider replaceable.
- Persist the exact request profile, response geometry, provider, calculation time, and
  returned warnings or restriction metadata.
- Render the primary route and supported alternatives with distance, duration, data
  freshness, provider, and limitations.
- Treat absent restriction data as unknown, never as proof of clearance.

**Exit criteria:** Every displayed route is traceable to its exact vehicle profile and
provider response; API, timeout, malformed-response, and no-route failures are tested.

## Phase 4 — Mandatory driver review

- Present a full-route overview and highlight warnings, restricted segments, and data gaps.
- Require the driver to compare the route with official restrictions, permits, dispatch
  guidance, and road signs before navigation can begin.
- Store the review acknowledgment locally with route/profile/time context without calling
  it certification or compliance approval.

**Exit criteria:** Navigation cannot begin for an unreviewed or materially changed route,
and all conflicting or incomplete data produces a visible warning.

## Phase 5 — Offline Route Corridor MVP

- Store the selected route, maneuvers, warnings, vehicle profile, provider, and timestamp.
- Download a configurable map corridor with explicit size, progress, expiry, cancellation,
  and deletion controls using a provider that permits offline prefetching.
- Display the saved route through connectivity loss while marking it offline/stale.
- Do not reroute offline. Leaving the stored corridor triggers a stop-and-reassess warning.

**Exit criteria:** The saved corridor works in airplane mode, storage limits and cleanup
are tested, provider terms are satisfied, and the app fails safely outside the corridor.

This is the first useful release boundary: reviewed vehicle-aware route planning plus
offline corridor viewing. Turn-by-turn guidance remains disabled.

## Phase 6 — Guided navigation

- Add location progress, maneuvers, voice prompts, off-route detection, and explicit GPS
  accuracy/staleness indicators.
- Define foreground/background behavior and privacy-preserving location retention.
- Permit online rerouting only after new restriction warnings are evaluated; retain the
  prohibition on automatic offline rerouting.

**Exit criteria:** GPS, connectivity, provider, and map-data failures always enter an
explicit degraded state; navigation and recovery tests pass on representative hardware.

## Phase 7 — Restriction and clearance assurance

- Identify authoritative federal, state, and local sources and record provenance,
  licensing, coverage, and update dates.
- Distinguish provider-derived restrictions from independently verified restrictions.
- Warn on unknown clearance and conflicting data instead of inferring permission.
- Add map-error reporting and verified scenarios for low clearances, weight limits, truck
  prohibitions, tunnels, hazmat restrictions, and temporary closures.

**Exit criteria:** Automated, simulation, and field-review evidence demonstrates safe
failure for missing, stale, and conflicting restriction data.

## Phase 8 — Controlled release

- Progress through internal routes and a closed professional-driver beta.
- Complete privacy, security, licensing, accessibility, and legal reviews.
- Measure route failures, warnings, and data gaps in addition to successful navigation,
  without collecting unnecessary precise-location history.
- Provide a remotely controlled routing kill switch that cannot silently weaken warnings.
- Keep the feature labeled beta until approved validation thresholds are met.

**Exit criteria:** Release criteria and evidence are approved by a human safety reviewer;
no unresolved safety-critical, licensing, privacy, or legal blocker remains.

