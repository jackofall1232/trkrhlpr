# Project Memory

Durable project facts and decisions that future agents should preserve.

## Decisions

- The final product name is **"Last Wagon"** (confirmed by the user 2026-07-16). It is the
  launcher label and applicationId (`com.lastwagon.app`) and appears throughout code and
  docs; the GitHub repository keeps its `trkrhlpr` name.
- trkrhlpr is Android-only and will initially be distributed as a sideloaded APK; Google
  Play support is not required for the first production milestone.
- Phase 5 corridor downloads use the OpenFreeMap public instance (Liberty style) for
  development and testing: no keys or request limits and commercial use allowed per the
  recorded research, but no explicit written bulk-prefetch clause was located, so corridor
  downloads stay bounded (small zoom ranges, 6,000-tile cap, one corridor at a time) and
  production use requires written terms confirmation or self-hosting. The demo MapLibre
  style must never be prefetched. See docs/map-provider-evaluation.md.
- **Architecture Decision:** Map routing will use the "Route Corridor" strategy.
  - **Visuals:** MapLibre GL Native for Android (offline-capable vector tiles).
  - **Routing:** OpenRouteService (ORS) is the initial online routing provider, using its
    `driving-hgv` profile and the driver-confirmed vehicle profile. The provider remains
    replaceable. Results are routing assistance based on available data, not a guarantee
    of safety, clearance, legality, or STAA compliance.
  - **Offline Capability:** The app will save selected route geometry and pre-fetch map
    resources along a bounded route corridor only from a provider whose terms permit it.
    The saved route remains visible through cell dead zones. Offline rerouting is not
    supported; leaving the corridor requires the driver to stop and reassess.
  - **Safety Contract:** The app must expose unverified, driver-reviewed, data-warning,
    and offline/stale route states. The driver must verify routes against signs, official
    restrictions, permits, dispatch instructions, and current conditions. Missing data is
    unknown, never proof of clearance. Release wording requires legal review and must not
    assume that a disclaimer transfers liability.
  - **Delivery Plan:** Implement the approved phases in
    `docs/truck-routing-plan.md`; the Offline Route Corridor at Phase 5 is the MVP boundary,
    with guided navigation and restriction assurance gated separately.
- The confirmed technology direction is native Kotlin, Jetpack Compose, Android SDK 36
  (Android 16), and an offline-first architecture.
- The first production milestone covers the 131-point pre-trip inspection, Study Mode,
  Real Inspection Mode, CDL practice tests, a daily safety question, and local progress.
- The national truck-stop directory, truck GPS/routing, online accounts, and cloud
  synchronization are future roadmap items excluded from the first milestone.
- Product content must be researched from FMCSA materials, official CDL manuals, and other
  lawful authoritative sources. Proprietary commercial study materials must not be copied.
- The 2026-07-13 protocol-initialization run did not authorize application implementation;
  the Android foundation was separately approved on 2026-07-13.
- Android architecture uses eight modules: app, core model/data/design/testing, and
  dashboard/learning/routing features.
- Minimum Android version is API 26; compile/target SDK is 36; Java target is 17.
- Manual constructor injection is sufficient for the current dependency graph.
- Room stores versioned content/progress; DataStore stores theme/accessibility preferences.
- SDK-36-compatible pins include Core 1.17.0 and Lifecycle 2.10.0.
- **The pre-trip checklist stays at 131 points** (owner confirmed the count question
  2026-07-16 by offering 100/101 only "if 131 point is not available" — it is available).
  "131" is Last Wagon's product enumeration, not an official term; no official source
  defines any fixed point count. Definition: `docs/pretrip-131-checklist.md`; sourcing and
  licensing rules: `docs/content-sourcing.md`.
- The AAMVA model CDL manual (including the FMCSA-hosted PDF) is AAMVA-copyrighted with
  SDLA-only reproduction rights: verification reference only — never copy its prose or
  commit the PDF. CFR text and FMCSA-authored materials are public domain (17 U.S.C.
  § 105). AAMVA's secure test item pool and commercial question banks are never sourced.
- The owner supplied a January 1, 2004 CVSA Out-of-Service Criteria scan (2026-07-16) as
  an internal reference only: CVSA-copyrighted and long superseded (annual revisions), so
  it is used for category-coverage cross-checks, never as a source of shipped text or
  figures, and must not be committed to the repository. Public defect criteria come from
  Appendix A to 49 CFR Part 396.
- **Next-week build-out (owner, 2026-07-16):** the owner wants a full build-out into a
  complete, useful app via Execution Mode (`.l00prite/prompts/execute-loop.md`). Approved
  scope = **first milestone + truck-stop directory** (Study Mode, Real Inspection Mode,
  CDL class + endorsement practice tests, mock exams, daily question, local progress, and
  the truck-stop dataset/search). Routing Phases 6-8 stay out of this build-out. Truck
  stops require their own public-data-source research gate (licensing/freshness) before
  implementation. Execution Mode is NOT armed yet — it requires the pre-flight display and
  an explicit in-session EXECUTE confirmation next week; this session only prepared the
  backlog.
- **CI (owner-approved 2026-07-16):** `.github/workflows/ci.yml` added — JDK 17 +
  Android SDK, `./gradlew assembleDebug check` on push/PR. Purpose: give the remote
  build-out loop real build verification, since remote sessions cannot reach dl.google.com
  but GitHub runners can. The build needs no secrets (ORS_API_KEY defaults to empty in
  app/build.gradle.kts). First real run happens on GitHub; if it fails, that is signal to
  fix, tracked via the PR-watch on #15.

## Facts

- Mission: "trkrhlpr exists to help commercial drivers perform safer inspections, become
  better educated, and reduce preventable violations through practical, offline-first tools."
- The initial audience is new and working commercial truck drivers in the United States.
- The product is intended as a practical daily companion, not merely exam preparation.
- Commercial-truck routing is safety-critical and must never be treated as ordinary
  passenger-car navigation.
- Exact inspection content, CDL questions, official sources, and unresolved product choices
  require research and explicit decisions before implementation.
- The foundation builds an installable debug APK at app/build/outputs/apk/debug/app-debug.apk.
- Phase 1 adds Internet and approximate-location permissions for an online read-only map.
  Approximate location is requested only from the location control; fine location is
  explicitly removed from the merged manifest. Accounts and analytics remain absent.
- Phase 1 uses MapLibre Native Android 13.0.2 behind a replaceable `MapStyleProvider`.
  MapLibre demo world data is evaluation-only, visibly attributed, and not approved as a
  production truck-map or offline-prefetch provider.
- Phase 2 stores one schema-versioned, confirmed commercial-vehicle profile in DataStore.
  US customary inputs are converted to canonical metric values. The profile includes type,
  dimensions, gross and axle weight, axle count, hazmat state, and supported avoidances.
  Broad plausibility validation is not a legal-limit or route-safety determination, and
  editing always requires the driver to reconfirm the current equipment and load.
- Phase 3 defines a replaceable `RoutingProvider` and uses ORS `driving-hgv` as the initial
  implementation. It sends the confirmed metric vehicle restrictions, supported toll/ferry
  avoidances, and road-access-restriction extra-info request. ORS does not support the
  profile's unpaved-road avoidance flag, so that preference is not sent and produces a
  visible warning. Results remain unverified and cannot start guidance.
- The last calculated route is schema-versioned and atomically stored in private app files
  with request/profile snapshot, geometry, summary, steps, warnings, restriction count,
  provider metadata, response hash, and timestamps. Changing the vehicle profile deletes
  the mismatched route; users can also delete it directly.
- Development ORS keys come from an uncommitted `ORS_API_KEY` Gradle property/environment
  value. They are embedded in the APK and must be treated as extractable. Production key
  handling requires a separate security decision before distribution.
- Phase 4 requires a full route overview and three separate driver acknowledgments before
  route-map access. The persisted review is bound to the exact provider request ID, vehicle
  profile confirmation time, review time, and acknowledgment schema. Legacy, mismatched,
  corrupt, or changed-profile records fail closed as unverified. Review is not certification.
- Route surfaces visibly expose `UNVERIFIED`, `DRIVER REVIEWED`, `DATA WARNING`, and
  `OFFLINE / STALE` states. Signs, officials, permits, closures, weather, and real-world
  conditions always override the application.

## Avoid
- Do not store random temporary notes, speculative ideas, or stale debugging output here.

## Initialization Handoff — 2026-07-13

- **Added:** Complete canonical l00prite memory, heartbeat, lock, prompt, event, review,
  session, Claude, Codex, and vendor-adapter support; project-specific guidance and a
  blueprint containing only confirmed product direction.
- **Why:** Establish durable, vendor-neutral project state and safety boundaries before any
  Android application development begins.
- **Validation evidence:** Canonical source validator exited 0; target doctor exited 0;
  JSON parsing, prompt parity, adapter parity, disarmed-state, no-placeholder, and
  no-Android-scaffold checks exited 0. Full command evidence is in `ledger.md`.
- **Remaining questions:** Authoritative checklist and CDL sources; checklist applicability;
  minimum Android version; module and storage architecture; accessibility; privacy and
  local-data lifecycle; content governance; detailed acceptance criteria; APK signing and
  update strategy. The prioritized list is in `todos.md`.
- **Recommended next step:** Human review of the initialized blueprint, followed by
  authoritative-source research before Android scaffolding is separately authorized.

## Android Foundation Handoff — 2026-07-14

- **Added:** Reproducible Android build; seven-module architecture; Room/DataStore offline
  persistence; industrial Compose design system; adaptive navigation; representative
  inspection, practice, daily, progress, and settings flows; unit/UI test sources.
- **Why:** Provide a scalable application shell before importing regulated content.
- **Validation:** Debug APK, JVM tests, instrumentation-test APK, and lint all build
  successfully. APK SHA-256 and exact commands are recorded in ledger.md.
- **Remaining:** Physical phone/tablet visual and connected-test review; authoritative
  content research; production signing/update process; detailed product acceptance criteria.
- **Recommended next step:** Install the APK on representative hardware and review the
  foundation before starting authoritative feature content.
