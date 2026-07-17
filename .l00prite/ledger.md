# Run Ledger

Append one entry per agent run. Do not overwrite prior runs.

## Entry Template

### Run YYYY-MM-DDTHH:MM:SSZ — <agent name>
- **Goal:** What this run attempted.
- **Triggering event:** Event id/type/source, or `none` for normal roadmap work.
- **Reviewer/comment reference:** PR, issue, CI run, reviewer, URL, file/line, or `none`.
- **Decision:** Valid, already fixed, unclear, unsafe, blocked, deferred, stale-lock-recovery, or normal work; include why.
- **Completed work:** What changed or was learned.
- **Fix implemented:** The smallest fix made for the event, or `none` with reason.
- **Changed files:** Files created, modified, deleted, or intentionally left untouched.
- **Tests run / Verification:** One entry per check run, each with `command`, `exit_code`,
  `summary`, `evidence_path` (optional), and `timestamp`. Do not write vague statements like
  "tests passed" without at least `command`, `exit_code`, and `summary`.
- **Response drafted/sent:** Reviewer, issue, or human response status and summary.
- **Event status:** Pending, processing, completed, blocked, deferred, or not applicable.
- **Failures:** Errors, blockers, failed approaches, or skipped checks.
- **Decisions:** Durable decisions made during the run.
- **Confidence:** Low/medium/high plus a short reason.
- **Next action:** The next smallest useful step.
- **Do-not-retry notes:** Failed approaches that should not be repeated unless conditions change.
- **Lock:** `lock_id` acquired/released this run, or `none` if no protected-path write occurred. Note stale-lock reclamation here if applicable.

## Runs

### Run 2026-07-13T23:01:32Z — Codex
- **Goal:** Initialize trkrhlpr with the current canonical l00prite protocol and record the confirmed product direction without scaffolding the Android application.
- **Triggering event:** `none` — explicit human-approved protocol initialization.
- **Reviewer/comment reference:** `none`.
- **Decision:** Normal Planning Mode initialization; install the complete protocol, preserve canonical prompt and adapter parity, initialize fresh project state, and keep unknown choices as TODOs.
- **Completed work:** Added canonical l00prite memory, lock, heartbeat, prompt, event, review, session, Codex, Claude, and vendor-neutral support; recorded the mission, Android/Kotlin/Compose/SDK 36 direction, offline-first milestone scope, roadmap exclusions, and authoritative-research gates.
- **Fix implemented:** `none` — initialization rather than an event fix.
- **Changed files:** Protocol and guidance files listed by the run's final git status; existing `README.md` and `LICENSE` intentionally unchanged; no application files created.
- **Tests run / Verification:**
  - `command`: `node /root/l00prite/scripts/validate-l00prite.js`; `exit_code`: `0`; `summary`: canonical source protocol validator passed all checks; `timestamp`: `2026-07-13T23:01:32Z`.
  - `command`: `node /root/l00prite/scripts/l00prite-doctor.js /root/trkrhlpr`; `exit_code`: `0`; `summary`: target memory had 24 OK, one pre-evidence advisory, and zero failures; `timestamp`: `2026-07-13T23:01:32Z`.
  - `command`: canonical/template `cmp` parity and JSON parse checks; `exit_code`: `0`; `summary`: all JSON parsed and all prompt, lock-document, and adapter copies matched canonical templates byte-for-byte; `timestamp`: `2026-07-13T23:01:32Z`.
  - `command`: placeholder, Android-scaffold, and disarmed-runtime checks; `exit_code`: `0`; `summary`: no generated placeholders, Kotlin/Gradle/manifest files, active execution, confirmation, or active lock were present; `timestamp`: `2026-07-13T23:01:32Z`.
  - `command`: `git diff --check`; `exit_code`: `0`; `summary`: no tracked whitespace errors; final untracked-file whitespace and scope checks are part of self-review; `timestamp`: `2026-07-13T23:01:32Z`.
  - `command`: final `node /root/l00prite/scripts/l00prite-doctor.js /root/trkrhlpr`; `exit_code`: `0`; `summary`: 25 OK, zero warnings, zero failures; Execution Mode disarmed; `timestamp`: `2026-07-13T23:01:32Z`.
- **Response drafted/sent:** Final initialization handoff prepared after successful validation.
- **Event status:** Not applicable.
- **Failures:** Initial sandbox policy required explicit approval to create `.codex/prompts/`; approval was received and no protocol scope changed.
- **Decisions:** Use only confirmed product facts; require authoritative and lawful sources; exclude application scaffolding and all future-roadmap systems.
- **Confidence:** High; initialization is derived from the current Git-tracked canonical templates and will be checked by the canonical validator and target doctor.
- **Next action:** Review the initialized blueprint, then research authoritative inspection and CDL sources before authorizing application scaffolding.
- **Do-not-retry notes:** Do not copy canonical-source dogfood runtime state or unrelated source-repository artifacts; do not invent content or unresolved architecture decisions.
- **Lock:** `none`; this atomic Planning Mode scaffold initialized new memory and left `lock.json` in its canonical `unlocked` state.

### Run 2026-07-13T23:43:57Z — Codex — Android foundation, unit 1
- **Goal:** Establish a reproducible Android 16 build toolchain and scalable module graph.
- **Triggering event:** `none` — approved Execution Mode foundation run.
- **Decision:** Use API 26 minimum, API 36 compile/target, AGP 9.2.1 built-in Kotlin 2.2.10,
  Gradle 9.4.1, JDK 17, and seven substantive modules.
- **Completed work:** Installed the official Android SDK command-line tools, platform 36,
  Build Tools 36.0.0, and platform tools; created Gradle configuration, version catalog,
  manifests, resources, and the approved module graph.
- **Changed files:** Root Gradle files, wrapper, `.gitignore`, `app/`, `core/`, and
  `feature/` build/manifests.
- **Tests run / Verification:** `command`: `./gradlew projects --stacktrace`;
  `exit_code`: `0`; `summary`: AGP configured and all seven modules resolved;
  `timestamp`: `2026-07-13T23:55:15Z`.
- **Failures:** None.
- **Decisions:** Keep manual constructor injection; use Room and DataStore; no network permission.
- **Confidence:** High; the build graph was resolved by the project wrapper.
- **Next action:** Implement versioned domain models, Room persistence, preferences, and repositories.
- **Do-not-retry notes:** Do not add the Kotlin Android plugin; AGP 9.2 uses built-in Kotlin.
- **Lock:** `72a233b9-f1af-4b43-a5bd-9c4d44483594` active and owned by this run.

### Run 2026-07-14T02:01:40Z — Codex — Android foundation, units 2–4
- **Goal:** Complete the production Android architecture, reusable UI foundation,
  representative flows, persistence, tests, documentation, and installable APK.
- **Triggering event:** none — continuation of the approved foundation run.
- **Decision:** Keep compile/target SDK 36 and pin the newest compatible stable AndroidX
  lines instead of violating scope by moving to API 37.
- **Completed work:** Added domain contracts; Room entities, DAO, database, and schema export;
  DataStore preferences; versioned sample import; reset transactions; a custom industrial
  design system; adaptive navigation; all representative screens; state restoration;
  reduced-motion behavior; tests; formatting policy; and build/sideload documentation.
- **Changed files:** app, core, feature, Gradle configuration, README, and l00prite memory.
- **Tests run / Verification:**
  - command: ./gradlew :core:model:testDebugUnitTest :core:data:testDebugUnitTest;
    exit_code: 0; summary: domain and sample-content tests passed; timestamp: 2026-07-14T02:01:40Z.
  - command: ./gradlew :core:designsystem:assembleDebug; exit_code: 0;
    summary: reusable Compose theme and components compiled; timestamp: 2026-07-14T02:01:40Z.
  - command: ./gradlew :app:assembleDebug testDebugUnitTest :app:assembleDebugAndroidTest;
    exit_code: 0; summary: app APK, JVM tests, and instrumentation-test APK built;
    timestamp: 2026-07-14T02:01:40Z.
  - command: ./gradlew lintDebug; exit_code: 0; summary: zero lint errors;
    evidence_path: app/build/reports/lint-results-debug.html; timestamp: 2026-07-14T02:01:40Z.
  - command: aapt dump badging app/build/outputs/apk/debug/app-debug.apk; exit_code: 0;
    summary: com.trkrhlpr.app, minSdk 26, target/compileSdk 36;
    evidence_path: app/build/outputs/apk/debug/app-debug.apk; timestamp: 2026-07-14T02:01:40Z.
- **Failures:** Current Compose rejected outdated positional APIs; corrected with named
  parameters. Core 1.19 and Lifecycle 2.11 require API 37; compatible stable versions were
  pinned for SDK 36. Instrumentation tests required an explicit test API opt-in.
- **Decisions:** No Internet permission, accounts, cloud, analytics, maps, routing, or
  authoritative content. Sample text is visibly labeled.
- **Confidence:** High for buildable architecture and representative behavior; physical
  phone/tablet visual and interaction testing remains.
- **Next action:** Run the APK and Compose tests on representative hardware, then research
  authoritative inspection and CDL sources before expanding content.
- **Do-not-retry notes:** Do not use Core 1.19 or Lifecycle 2.11 with compileSdk 36; do not
  use positional Compose calls where API order can drift.
- **Lock:** Prior lock expired during long builds and was reclaimed by the same run as
  e56ee8cb-e67c-411a-83cb-ed938f6a127d.

### Run 2026-07-14T02:05:28Z — Codex — Android foundation exit
- **Goal:** Perform final verification, self-review, persistence, and handoff.
- **Decision:** Stop at human_review_gate after delivering the buildable foundation;
  physical phone/tablet visual review and connected UI execution remain human/device work.
- **Completed work:** Corrected reset feedback, persisted daily completion on revisit,
  reduced-motion animation behavior, process recreation for practice selection, adaptive
  orientation support, and deprecated icon usage.
- **Tests run / Verification:**
  - command: ./gradlew :app:assembleDebug testDebugUnitTest :app:assembleDebugAndroidTest lintDebug;
    exit_code: 0; summary: 344-task final suite successful; timestamp: 2026-07-14T02:05:28Z.
  - command: sha256sum app/build/outputs/apk/debug/app-debug.apk; exit_code: 0;
    summary: 5c8eed575ab0b3adcdb3d294c58a0862cb71d1a460b103b01faf1b4c6c423b32,
    20107325 bytes; timestamp: 2026-07-14T02:05:28Z.
  - command: git diff --check; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-14T02:05:28Z.
- **Failures:** Connected UI tests were not run because this host has no device, emulator,
  or KVM. Their APK compiles successfully.
- **Confidence:** High in build integrity and architecture; medium-high in rendered UX until
  human device review confirms appearance, touch ergonomics, and adaptive layouts.
- **Next action:** Install the debug APK on representative phone/tablet hardware and run
  connectedDebugAndroidTest, then review the foundation before authoritative content work.
- **Do-not-retry notes:** Do not claim connected tests ran in this environment.
- **Lock:** e56ee8cb-e67c-411a-83cb-ed938f6a127d released at mode exit.

### Run 2026-07-14T02:07:03Z — Codex — Final self-review correction
- **Goal:** Remove an empty-module abstraction and reconcile final protected memory.
- **Completed work:** Added reusable fake content, progress, and preference repositories to
  core:testing; removed the stale "None yet" failure entry; clarified that device review is
  a human gate rather than a foundation blocker.
- **Tests run / Verification:** command: ./gradlew :core:testing:assembleDebug
  :core:testing:lintDebug; exit_code: 0; summary: shared test support compiled and linted;
  timestamp: 2026-07-14T02:07:03Z.
- **Next action:** Commit the verified foundation for human review.
- **Lock:** 53680f65-a741-48fe-a5d7-7adcd878ace6 acquired and released for final persistence.

### Run 2026-07-14T02:20:05Z — Codex — PR #2 review corrections
- **Goal:** Resolve all five valid Android-foundation review comments without widening scope.
- **Triggering event:** event-20260714-022005-github-pr2-foundation-review-a5f2.
- **Completed work:** Removed a redundant Room primary-key index; made persisted theme parsing
  tolerant of unknown values; replaced independently observed attempt counters with one atomic
  statistics query; made Compose state collection lifecycle-aware; and hoisted the static
  top-level route set. Added regression coverage for preference parsing and regenerated the
  unshipped version-1 Room schema.
- **Tests run / Verification:**
  - command: ./gradlew :core:data:testDebugUnitTest :app:compileDebugKotlin; exit_code: 0;
    summary: targeted data tests and application Kotlin compilation passed;
    timestamp: 2026-07-14T02:20:05Z.
  - command: ./gradlew :app:assembleDebug testDebugUnitTest :app:assembleDebugAndroidTest lintDebug;
    exit_code: 0; summary: full 346-task APK, JVM test, instrumentation APK, and lint suite passed;
    timestamp: 2026-07-14T02:20:05Z.
  - command: git diff --check; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-14T02:20:05Z.
- **Failures:** None.
- **Decisions:** All five comments were valid. Updating the version-1 Room schema is safe because
  no production database has shipped; a migration is not required.
- **Confidence:** High.
- **Next action:** Commit and push the corrections to PR #2, then await reviewer confirmation.
- **Lock:** 8f3d85b2-b767-4f22-910f-441c5ca29fab acquired for the review loop.

### Run 2026-07-16T12:00:00Z — Gemini — Update project scope TODOs
- **Goal:** Update the project TODOs with new requirements for a comprehensive trucking app.
- **Triggering event:** User prompt to include STAA routing, bridge height, truck stops, and CDL mock exams.
- **Completed work:** Updated `.l00prite/todos.md` to elevate and expand requirements for truck routing, bridge heights, truck stops, and mock exams.
- **Lock:** c1a2b3c4-d5e6-7f8a-9b0c-1d2e3f4a5b6c acquired and released.

### Run 2026-07-16T12:05:00Z — Gemini — Document Route Corridor strategy
- **Goal:** Formalize the "Route Corridor" map architecture decision in project memory.
- **Triggering event:** User confirmed the MapLibre + OpenRouteService (ORS) strategy.
- **Completed work:** Updated `.l00prite/memory.md` to document the decision to use MapLibre vector tiles and an ORS backend for truck-safe route geometry, with offline tile pre-fetching. Updated `.l00prite/todos.md` with prioritized tasks for implementing this architecture.
- **Lock:** b7a2d4f8-e1c9-4b36-a857-9c12b4e6d3f0 acquired and released.

### Run 2026-07-16T16:22:26Z — Codex — Approve phased truck-routing plan
- **Goal:** Persist the human-approved, manageable implementation plan for commercial-truck
  map routing and prepare it for review against `0.0.1-alpha`.
- **Triggering event:** User approved the proposed phases and requested durable memory plus
  a mergeable pull request.
- **Completed work:** Added `docs/truck-routing-plan.md` with Phases 0–8, explicit exit
  criteria, a Phase 5 offline Route Corridor MVP boundary, and later gates for guided
  navigation, restriction assurance, and controlled release. Replaced guarantee and
  STAA-compliance claims in durable memory and the blueprint with qualified decision-support
  language, mandatory driver verification, explicit degraded states, and legal review.
  Reorganized routing TODOs by phase and updated state/heartbeat for human review.
- **Tests run / Verification:**
  - command: `git diff --check`; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-16T16:22:26Z.
  - command: parse `.l00prite/lock.json`, `state.json`, and `heartbeat.json` with Node;
    exit_code: 0; summary: modified protocol JSON remained valid;
    timestamp: 2026-07-16T16:22:26Z.
  - command: scan for superseded guarantee/STAA TODO wording; exit_code: 0;
    summary: no superseded claims remained; timestamp: 2026-07-16T16:22:26Z.
- **Failures:** Ruby and jq were unavailable for JSON validation; Node validation succeeded.
- **Decision:** Planning only; no routing code, dependencies, permissions, providers, or
  external data were added. Routing remains outside the first production milestone.
- **Confidence:** High for plan consistency; implementation, provider licensing, field
  validation, and legal review remain future gated work.
- **Next action:** Review and merge the plan PR, then begin Phase 0 as a separately approved
  unit before any map implementation.
- **Lock:** 2c9671d7-a175-45f1-a604-31e53281087e acquired and released.
- **PR handoff:** Commit `2f9acfb` was created on
  `docs/phased-truck-routing-plan`. Push failed because the environment has no GitHub
  credentials (`fatal: could not read Username for 'https://github.com'`). Authenticate
  GitHub, push the branch, and open the PR against `0.0.1-alpha`; no retry was claimed.
- **SSH follow-up (2026-07-16T16:30:45Z):** Verified the existing SSH key as
  `jackofall1232`, changed the clone's remote to SSH, and successfully pushed
  `docs/phased-truck-routing-plan`. `gh pr create` could not create the PR because GitHub
  CLI has no API authentication; SSH authentication alone does not authenticate the CLI.
  The branch is available at GitHub's PR creation URL.

### Run 2026-07-16T16:43:23Z — Codex — Phase 1 read-only MapLibre map
- **Goal:** Implement the approved Phase 1 map foundation on `0.0.2-alpha` without adding
  route calculation or offline prefetching.
- **Triggering event:** User explicitly requested Phase 1 implementation on `0.0.2-alpha`.
- **Completed work:** Added the `feature:routing` module with MapLibre Native Android
  13.0.2, a Compose-hosted lifecycle-aware `MapView`, replaceable `MapStyleProvider`, zoom
  controls, visible evaluation limitations and attribution, and opt-in approximate location
  with denial handling. Added dashboard/navigation entry, Internet and coarse-location
  permissions, a provider evaluation record, unit/navigation tests, and accurate README
  privacy/module documentation. Explicitly removed MapLibre's transitive fine-location
  permission from the packaged manifest.
- **Provider decision:** The token-free MapLibre demo world style is approved only for
  development/evaluation because it is MapLibre's documented quickstart/example source.
  It is not a production truck-map or offline-prefetch provider. Phase 5 still requires a
  separate licensing, attribution, availability, privacy, caching, and offline-terms review.
- **Tests run / Verification:**
  - command: `env ANDROID_HOME=/opt/android-sdk ./gradlew :feature:routing:testDebugUnitTest
    :app:assembleDebug :app:assembleDebugAndroidTest lintDebug`; exit_code: 0; summary:
    routing unit test, debug APK, instrumentation-test APK, and full lint suite passed;
    timestamp: 2026-07-16T16:43:23Z.
  - command: `aapt dump permissions app/build/outputs/apk/debug/app-debug.apk`; exit_code: 0;
    summary: Internet and coarse location present; fine location absent;
    timestamp: 2026-07-16T16:43:23Z.
  - command: `git diff --check`; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-16T16:43:23Z.
  - artifact: `app/build/outputs/apk/debug/app-debug.apk`; SHA-256:
    `94aacfbb801553cb058a4cb15dd51a25d9313530e821cba8add195c8ce0972ad`.
- **Failures:** The first Gradle invocation lacked `ANDROID_HOME`; the SDK was found at
  `/opt/android-sdk` and subsequent verification succeeded. The initial packaged manifest
  exposed MapLibre's transitive fine-location declaration; it was removed and the package
  was rebuilt and re-audited.
- **Decision:** No routing, route claims, background location, telemetry, tile prefetching,
  or offline-region download was implemented. Core learning/inspection behavior remains
  offline. Device execution was not available in this environment.
- **Confidence:** High for build/lint/package integrity; medium-high for runtime rendering
  until representative phone/tablet checks exercise lifecycle, network loss, and location
  grant/denial.
- **Next action:** Complete the Phase 1 physical-device exit check before beginning Phase 2.
- **Lock:** e47059cc-d5cf-48c9-b60a-759978ed8131 acquired for this implementation run.

### Run 2026-07-16T17:18:36Z — Codex — Phase 1 lifecycle fix and Phase 2 vehicle profile
- **Goal:** Address the Phase 1 lifecycle review and implement a validated,
  driver-confirmed commercial-vehicle profile without beginning route calculation.
- **Triggering event:** User reported review feedback after merging Phase 1 and explicitly
  requested the small fix plus Phase 2 in one follow-up branch.
- **Completed work:** Removed unconditional MapView start/resume calls, refreshed coarse
  location permission state on every host resume, and tracked active start/resume states so
  navigation disposal still performs required pause/stop cleanup before destroy. Added the
  Phase 2 domain model, conversion utilities, plausibility validator, schema-versioned
  DataStore repository, fake repository, profile editor/summary, driver confirmation and
  reconfirmation, and map gating behind a saved profile. Captured vehicle type, dimensions,
  gross/axle weights, axle count, hazmat, toll/ferry/unpaved avoidances, and confirmation time.
- **Tests run / Verification:**
  - command: `env ANDROID_HOME=/opt/android-sdk ./gradlew testDebugUnitTest
    :app:assembleDebug :app:assembleDebugAndroidTest lintDebug`; exit_code: 0; summary:
    all JVM tests, debug APK, instrumentation-test APK, and full lint suite passed;
    timestamp: 2026-07-16T17:18:36Z.
  - tests: unit conversion round trips; valid/invalid profile bounds; confirmation; current,
    unknown, corrupt, and invalid persistence records; provider contract; existing data tests.
  - command: `aapt dump permissions app/build/outputs/apk/debug/app-debug.apk`; exit_code: 0;
    summary: Internet and coarse location present; fine location absent;
    timestamp: 2026-07-16T17:18:36Z.
  - command: `git diff --check`; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-16T17:18:36Z.
  - artifact: `app/build/outputs/apk/debug/app-debug.apk`; SHA-256:
    `f12190246b97820d20e1d31254a1720f8c023f99cfb80c195183cf73b3d934c7`.
- **Failures:** Initial compilation exposed a missing routing-to-model dependency, corrected
  KeyboardOptions import, and heterogeneous preference-key removal; each was corrected
  before the successful full suite.
- **Decisions:** Inputs use US customary units for the initial US audience and are persisted
  canonically in metric units for later routing-provider requests. Plausibility ranges are
  deliberately broad input-error guards, not assertions of legal size/weight or route safety.
  No route calculation, automatic defaults, or background location was added.
- **Confidence:** High for compilation, persistence parsing, validation, and package
  permissions; medium-high for UX until profile entry and map lifecycle are device-tested.
- **Next action:** Review both phases on representative hardware, then begin Phase 3 only
  after approving the routing API contract and credential handling.
- **Lock:** 334326fe-19b9-46d9-806e-fcf649892fad acquired and released.

### Run 2026-07-16T17:38:22Z — Codex — Phase 3 ORS route preview
- **Goal:** Implement a replaceable online HGV routing provider with exact request/response
  provenance, explicit failure behavior, saved geometry, and an unreviewed map preview.
- **Triggering event:** User requested Phase 3 implementation on `0.0.3-alpha`.
- **Completed work:** Added routing domain contracts, ORS `driving-hgv` GeoJSON client,
  injectable HTTP transport, supported vehicle restrictions and avoidances, restriction
  metadata parsing, timeout/network/quota/provider/no-route/malformed/missing-key failures,
  and response hashing. Added coordinate entry, unreviewed summary and warnings, MapLibre
  route overlay/fit, atomic schema-versioned private-file persistence, route deletion, and
  invalidation whenever the vehicle profile changes. Added local-only ORS key configuration
  and documented that client APK credentials are extractable.
- **Tests run / Verification:**
  - command: `env ANDROID_HOME=/opt/android-sdk ./gradlew testDebugUnitTest
    :app:assembleDebug :app:assembleDebugAndroidTest lintDebug`; exit_code: 0; summary:
    all JVM tests, debug APK, instrumentation-test APK, and full lint suite passed;
    timestamp: 2026-07-16T17:38:22Z.
  - tests: exact HGV restriction payload; supported/unsupported avoidances; geometry,
    summary, restriction extras, warnings, and provider metadata parsing; missing key and
    rate limit; route persistence round-trip; unknown schema and corrupt geometry rejection.
  - command: `aapt dump permissions app/build/outputs/apk/debug/app-debug.apk`; exit_code: 0;
    summary: Internet and coarse location present; fine location absent;
    timestamp: 2026-07-16T17:38:22Z.
  - command: `git diff --check`; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-16T17:38:22Z.
  - artifact: `app/build/outputs/apk/debug/app-debug.apk`; SHA-256:
    `eb109f94d2af603b8bcb882e5d46a863f3fc1c39a7164c9692bb854a13453b9e`.
- **Failures:** An expression-body return, missing coroutine-test dependency, and subsequent
  compile issues were corrected before the successful suites. No live ORS call ran because
  this environment intentionally had no API key.
- **Decisions:** ORS supports toll/ferry but not unpaved-road avoidance; never silently send
  an invented flag. Store the exact key-free payload and response SHA-256 rather than raw
  provider response. Coordinate entry is the Phase 3 boundary; geocoding is not fabricated.
  Every route remains unverified, and Phase 4 owns mandatory driver review.
- **Confidence:** High for the request/parser/storage contracts and build integrity; medium
  for live-provider behavior until controlled keyed integration and device tests pass.
- **Next action:** Exercise a restricted development key and representative failure cases,
  then implement Phase 4 only after reviewing the resulting route-overview UX.
- **Lock:** 4a5ca238-e44e-4ad7-979a-a8ec31c2c181 acquired and released.

### Run 2026-07-16T17:54:44Z — Codex — Phase 4 mandatory driver review
- **Goal:** Require a comprehensive, persisted driver review of the exact route and vehicle
  profile before route-map access, without implying certification or beginning navigation.
- **Triggering event:** User requested Phase 4 on `0.0.4-alpha` and asked whether Phase 5
  would form the first end-to-end testable MVP boundary.
- **Completed work:** Added a versioned `RouteReview`, exact request/profile matching, and
  repository-level review recording. Migrated saved-route schema from 1 to 2 while loading
  Phase 3 routes as unverified. Added full overview of endpoints, summary, vehicle/load,
  warnings, restriction gaps, steps, and provenance; three mandatory acknowledgments; and
  visible unverified/reviewed/data-warning/offline-stale states. Gated all route-map access
  on a valid current review and documented that review is not certification.
- **Tests run / Verification:**
  - command: `env ANDROID_HOME=/opt/android-sdk ./gradlew testDebugUnitTest
    :app:assembleDebug :app:assembleDebugAndroidTest lintDebug`; exit_code: 0; summary:
    JVM tests, debug APK, instrumentation-test APK, and full lint suite passed;
    timestamp: 2026-07-16T17:54:44Z.
  - tests: reviewed-route round trip; mismatched request/profile rejection; Phase 3 schema
    migration to unreviewed; fail-closed map gate for absent/mismatched reviews.
  - command: `aapt dump permissions app/build/outputs/apk/debug/app-debug.apk`; exit_code: 0;
    summary: Internet and coarse location present; fine location absent;
    timestamp: 2026-07-16T17:54:44Z.
  - command: `git diff --check`; exit_code: 0; summary: no whitespace errors;
    timestamp: 2026-07-16T17:54:44Z.
  - artifact: `app/build/outputs/apk/debug/app-debug.apk`; SHA-256:
    `9ac4441b9b3b2c6181de174fe1814a24077a7be88dddecf4e6a107ee251cf2ca`.
- **Failures:** None remained after implementation; targeted and full suites passed.
- **Decisions:** Map preview is gated along with future guidance to make bypass impossible
  at this stage. Acknowledgments are separate, locally persisted, and invalidated by any
  request/profile mismatch. Phase 5 is the first integrated MVP boundary, but Phase 4 must
  still be tested independently before offline complexity is added.
- **Confidence:** High for persistence, migration, and programmatic gating; medium-high for
  review UX until long content, accessibility, and bypass attempts are checked on hardware.
- **Next action:** Perform Phase 4 device review, then implement licensed offline corridor
  storage and safe off-corridor behavior in Phase 5.
- **Lock:** c983a67c-6249-4ea9-89df-09031d296c97 acquired and released.

### Run 2026-07-16T18:58:16Z — Claude — Last Wagon rename and Phase 5 offline corridor
- **Goal:** Rename the product to its final name "Last Wagon" everywhere and implement
  Routing Phase 5 (offline route corridor MVP boundary) so end-to-end truck routing can be
  tested on hardware.
- **Triggering event:** User confirmed the final app name "Last Wagon", asked for it to be
  reflected everywhere, and requested Phase 5 build-out on branch
  `claude/last-wagon-truck-routing-1fq6h7`.
- **Completed work:** Renamed the launcher label, applicationId/namespaces
  (`com.trkrhlpr` → `com.lastwagon`), application/database/theme/design-token identifiers,
  Gradle root project, and product references in README/AGENTS/CLAUDE/blueprint/memory;
  the GitHub repository keeps its `trkrhlpr` name. Implemented Phase 5: corridor metadata
  (schema v1) bound to the exact route request id and vehicle-profile confirmation time;
  MapLibre offline geometry region download along the reviewed route with three detail
  levels, pre-download tile estimation, a 6,000-tile hard cap, live progress/size,
  cancellation, deletion, and 7-day expiry with a visible stale state; per-provider
  offline-prefetch permission with the demo provider forbidden and OpenFreeMap Liberty
  adopted for development/testing (terms research and an explicit production-confirmation
  gate recorded in docs/map-provider-evaluation.md); connectivity monitoring with explicit
  OFFLINE/STALE route states; route calculation disabled offline and no offline rerouting
  anywhere; a coarse-location off-corridor stop-and-reassess warning that subtracts
  reported accuracy so an imprecise fix never raises a false alarm; ACCESS_NETWORK_STATE
  permission added for connectivity monitoring.
- **Tests run / Verification:**
  - command: standalone K2 compile + JUnit (kotlin-compiler 2.2.10, kotlinx-serialization
    1.9.0, kotlinx-coroutines 1.10.2, JUnit 4.13.2 from Maven Central); exit_code: 0;
    summary: OfflineCorridorTest, GeoMathTest, MapStyleProviderTest — 16 tests, all
    passing; timestamp: 2026-07-16T18:56Z.
  - command: standalone K2 compile of OfflineCorridorManager.kt, NetworkMonitor.kt, and
    FileRouteRepository.kt against org.maplibre.gl:android-sdk:13.0.2 classes.jar,
    android-sdk-geojson 6.0.1, and Robolectric android-all API 36; exit_code: 0; summary:
    all MapLibre offline API usage (OfflineManager, OfflineGeometryRegionDefinition,
    OfflineRegion observer/status/delete, tile count limit) verified against the real
    artifact; timestamp: 2026-07-16T18:56Z.
  - **NOT verified here:** full Gradle build, lint, Compose UI compilation, APK assembly,
    and instrumentation-test compilation. This remote environment's network policy blocks
    dl.google.com (Google Maven and Android SDK components), so AGP/androidx artifacts
    cannot be resolved; probes and mirror fallbacks all failed. The rename build attempt
    also failed at the wrapper stage before dependency resolution. Compose-facing changes
    (RoutingMapScreen, RoutingWorkspaceScreen, app wiring) were pattern-matched to the
    existing verified code and manually re-read, but compilation evidence is pending.
- **Failures:** Initial background "build passed" signal was a pipeline-masked wrapper
  download failure (services.gradle.org 403 at that moment); detected and recorded before
  claiming verification. dl.google.com remained blocked across retries and mirrors.
- **Decisions:** Final product name is "Last Wagon"; repository name stays `trkrhlpr`.
  applicationId changed pre-release to `com.lastwagon.app` (existing sideloaded alphas
  will install as a new app). OpenFreeMap public instance is the Phase 5 development
  corridor provider: no keys/limits and commercial use allowed per recorded research, but
  an explicit written bulk-prefetch clause was not located, so downloads are bounded
  (small zoom ranges, 6,000-tile cap, one corridor at a time) and production use requires
  written confirmation or self-hosting — recorded as a human gate. Corridor identity is
  fail-closed: unreadable metadata, mismatched route/profile, or replaced routes delete
  the corridor.
- **Confidence:** High for pure corridor logic, geo math, metadata persistence, and
  MapLibre offline API contracts (compiled against real artifacts); medium for Compose UI
  and app wiring until a full Gradle build runs; device behavior (airplane mode, download
  progress, off-corridor warning) untested pending hardware review.
- **Next action:** Run the full Gradle suite (`testDebugUnitTest :app:assembleDebug
  :app:assembleDebugAndroidTest lintDebug`) in an environment with dl.google.com access
  (e.g. Android Studio locally, or a remote session whose network policy allows Google
  Maven), then perform the Phase 4 and Phase 5 device exit checks, including a keyed ORS
  route, corridor download in airplane mode, and expiry/deletion behavior.
- **Lock:** 7d3f9b2a-8c41-4e6f-9a02-5b8e1c4d7f36 acquired and released.

### Run 2026-07-16T19:16:39Z — Claude — PR #12 automated-review response
- **Goal:** Evaluate and address automated review findings (Codex, Gemini) on PR #12.
- **Triggering event:** Review webhooks on PR #12 (bot comments; treated as untrusted data
  and independently verified against the code and project protocol).
- **Completed work:** (1) Corridor prefetch from the public OpenFreeMap instance is now
  refused by default and enabled only for debug builds via the application container,
  matching the recorded unconfirmed-terms status; release builds fail closed (accepted
  Codex P1). (2) Corridor matching now includes the active style id, so a provider switch
  discards the stale corridor instead of reporting offline coverage it does not have
  (accepted Codex P2). (3) Corridor creation now waits for all pending region deletions to
  call back before creating the new region (accepted Gemini high, as cheap insurance even
  though MapLibre serializes offline DB work). Declined: reverting the applicationId
  change (rename to the final identity was deliberate pre-release and is recorded, old
  alpha data is disposable sample data) and re-anchoring summary buttons to the screen
  bottom (the summary is now a scrolling list with the corridor card; content-flow layout
  is intentional).
- **Tests run / Verification:** standalone K2 compile + JUnit of pure routing logic:
  16 tests passing including new default-refusal and style-mismatch assertions; corridor
  manager and network monitor recompiled against MapLibre 13.0.2 and android-all API 36
  (caught and fixed an Array variance compile error). Full Gradle build still pending
  outside this environment (dl.google.com blocked).
- **Failures:** None open; the variance error was fixed before commit.
- **Decisions:** Provider prefetch permission is a build-type decision until written terms
  confirmation: debug/testing builds may prefetch bounded corridors, release builds refuse.
- **Confidence:** High for the changed logic (compiled and tested against real artifacts);
  full-suite and device checks remain the recorded exit gates.
- **Next action:** Reply to the review threads, keep watching PR #12 until merged/closed.
- **Lock:** 8156521d-70d3-479a-bb49-582cdeddeb2a acquired and released.

### Run 2026-07-16T19:22:34Z — Claude — PR #12 second review round
- **Goal:** Address Codex findings on commit 0c6f99d.
- **Triggering event:** Codex re-review webhooks on PR #12.
- **Completed work:** Corridor downloads now fail closed when existing offline regions
  cannot be listed: the download aborts with a visible failure and asks for a retry
  instead of creating a region that could coexist with an unaccounted-for prior corridor
  (accepted P2). For the AGENTS.md P1: the flagged edit is the one-line product-name
  mission statement, changed on the user's explicit instruction that the final name
  "Last Wagon" be reflected everywhere; that direct human instruction is the
  authorization the protocol's human review gate requires, and it is recorded here and
  in the 2026-07-16T18:58:16Z run entry. No operating rule, gate, boundary, or prompt
  text was changed. Recorded explicitly: **the AGENTS.md mission-line rename was
  human-authorized; no protocol rule was modified.**
- **Tests run / Verification:** OfflineCorridorManager recompiled standalone against
  MapLibre 13.0.2 and android-all API 36; exit 0. Pure-logic tests unchanged (16 passing
  as of the prior run). Full Gradle suite still pending outside this environment.
- **Failures:** None.
- **Decisions:** Listing failures block downloads (fail closed) rather than best-effort
  creating; the one-corridor invariant outranks download convenience.
- **Confidence:** High for the changed branch; unchanged elsewhere.
- **Next action:** Keep watching PR #12 until merged or closed.
- **Lock:** 4db86b82-3631-4b4c-937c-86a6ea8ea208 acquired and released.

### Run 2026-07-16T19:30:38Z — Claude — PR #12 third review round
- **Goal:** Address Codex findings on commit 04afa97.
- **Triggering event:** Codex re-review webhooks on PR #12.
- **Completed work:** (1) Download setup is now generation-stamped: cancel, delete, and
  refresh invalidate in-flight list/delete/create chains, and a region created after
  cancellation is discarded instead of adopted, so cancelling during "Preparing" really
  cancels. (2) discardAllThen reports the first deletion error and creation is aborted,
  so a replacement corridor is never created while an old one may still be stored.
  (3) The route map now shows "Off-route warning UNAVAILABLE — needs approximate
  location" whenever a route is displayed but no location fix is available to check the
  corridor boundary, instead of silently skipping the stop-and-reassess safety net.
- **Tests run / Verification:** OfflineCorridorManager and NetworkMonitor recompiled
  standalone against MapLibre 13.0.2 and android-all API 36; exit 0. Pure-logic suites
  unchanged (16 passing). Full Gradle suite still pending outside this environment.
- **Failures:** None.
- **Decisions:** All corridor lifecycle callbacks are generation-checked on the main
  thread; safety warnings fail visible (unknown boundary is announced, never assumed
  inside).
- **Confidence:** High for the changed logic; device review remains the exit gate.
- **Next action:** Keep watching PR #12 until merged or closed.
- **Lock:** ddaf54d0-aad0-4924-9e06-928b67510399 acquired and released.

### Run 2026-07-16T19:59:53Z — Claude — Marketing website v1 scaffold
- **Goal:** Build the Last Wagon marketing/landing site (lastwagon.app) per the
  user-approved prompt and visual direction board.
- **Triggering event:** Direct user request in a Claude Code remote session on branch
  `claude/last-wagon-marketing-site-jytti2`, with the direction-board HTML supplied.
- **Completed work:** New `website/` Next.js 15 + TypeScript + Tailwind 3 app (App
  Router), isolated from the Gradle project. Locked palette as Tailwind theme tokens;
  Archivo (variable, wdth axis) + Space Mono via next/font; hero scene (gradient sky,
  sun, 3 parallax ridgelines, dashed road, traveling rig SVG) ported verbatim from the
  direction board; features cards with the three illustration motifs; how-it-works with
  a static route-preview mockup (scroll-drawn route line) and the pre-trip checklist UI
  mock; download CTA (placeholder APK link); mono footer. Direction board committed at
  `website/design/direction-board.html` as the canonical reference.
- **Tests run / Verification:** `npm run build` clean (all routes static). Served
  production build and verified in Chromium via Playwright: desktop 1440px and mobile
  390px full-page screenshots, `prefers-reduced-motion` (rig parks at left 20%, route
  line pre-drawn), computed h1 style (Archivo, font-stretch 125%, weight 800), and the
  scroll-triggered route-line draw completing.
- **Failures:** None in the product. Notable finding: "Archivo Expanded" is not a real
  Google Fonts family (CSS API returns 400) — the direction board's <link> silently
  fell back; implemented as Archivo variable wdth=125 via font-stretch instead.
- **Decisions:** Site lives in `website/` to keep the Android app tree clean; Tailwind 3
  with theme-extend tokens (no colors outside the locked palette); no API routes/ISR so
  a static-export fallback stays open; dark-mode only per the locked design; APK link
  and final CTA copy left as explicit placeholders pending distribution approval.
- **Confidence:** High for build/render correctness; Vercel deployment intentionally not
  performed (user asked to be consulted before deploying).
- **Next action:** User review of the site; then real CTA copy, signed-APK link, and an
  explicitly approved Vercel deploy.
- **Push authorization:** This remote session's task instructions explicitly designate
  branch `claude/last-wagon-marketing-site-jytti2` and instruct commit + push there;
  that per-branch instruction is the recorded authorization for this push. No merge,
  deploy, or PR was performed.
- **Lock:** 48f940f2-67fd-4d6a-90f6-1cdfa0d7a49d acquired and released.

### Run 2026-07-16T20:06:22Z — Claude — Website design-improvement pass
- **Goal:** Improve on the direction-board draft where warranted; the user explicitly
  released the verbatim-port constraint ("if you see room for design improvement you may
  make improvements").
- **Triggering event:** Direct user message in the same remote session.
- **Completed work:** Hero: starfield with staggered twinkle, horizon-haze slats masked
  into the sun disc, film-grain overlay to kill gradient banding, animated exhaust puffs
  (unclipped via extended viewBox), headlight and wheel-rim details on the rig; slim
  overlay header (canopy glyph wordmark + always-reachable GET THE APK); feature cards
  gained mono indices, amber border hover, and horizontal motif drift; how-it-works steps
  threaded with a dashed route connector; route mock gained waypoint stops and a pulsing
  position dot; checklist mock gained an 87-of-131 progress bar; download section gained
  a radial amber glow and a low-opacity steel ghost rig traveling its bottom border. Rig
  geometry refactored into color-parameterized RigIllustration; smooth anchor scrolling
  (motion-safe only). All new motion is steady/linear per the motion language.
- **Tests run / Verification:** Clean static `npm run build`; Chromium screenshots at
  1440px and 390px; reduced-motion computed-style checks (hero rig parked at 20%, ghost
  rig at 12%, smoke/twinkle/pulse animations off, route line pre-drawn).
- **Failures:** None.
- **Decisions:** Palette, type system, and motif language stay locked; improvements are
  execution-level only. Ghost rig at 0.4 opacity so it reads as texture, not content.
- **Confidence:** High; visually verified.
- **Next action:** User review; CTA copy, signed-APK link, and Vercel deploy still
  pending explicit approval.
- **Push authorization:** Same designated-branch instruction as the prior run.
- **Lock:** 78b835f0-1d51-4250-950a-0833b3c01f44 acquired and released.

### Run 2026-07-16T20:23:12Z — Claude — PR #13 review response (Codex + Gemini)
- **Goal:** Triage the first review round on PR #13 (marketing website).
- **Triggering event:** Codex and Gemini review webhooks.
- **Completed work:** Codex raised three P1s that the feature copy claims unbuilt or
  unverified capabilities (truck-stop directory, verified 131-point checklist,
  "authoritative" CDL material). The findings match the blueprint's scope, so they were
  escalated to the human owner rather than self-resolved; the owner decided the copy
  stays ("there will be those things before the public sees the site") — recorded here
  as the authorization to leave launch-scope claims in place, revisitable before any
  public deployment. Gemini fixes applied: direction-board.html now loads the real
  Archivo variable font (wdth axis; the old "Archivo Expanded" family URL returned 400)
  with font-stretch:125% on display selectors, and the header wordmark links to "/"
  instead of "#". Gemini's useId suggestion declined: RigScene is a single-use server
  component with namespaced SVG ids; converting it to a client component would ship JS
  for decorative art.
- **Tests run / Verification:** Clean static build; corrected Google Fonts URL verified
  HTTP 200 (old URL 400).
- **Failures:** None.
- **Decisions:** Marketing copy describes v1 launch scope per explicit owner decision;
  technical review fixes applied where they improve correctness without cost.
- **Confidence:** High.
- **Next action:** Keep watching PR #13 until merged or closed; hourly self check-in
  armed (trig_01AUgifGrKFyFiutPPMnyqqJ).
- **Push authorization:** Same designated-branch instruction as prior runs.
- **Lock:** 09ceea1d-6f25-4f23-91b2-8033f43b8955 acquired and released.

### Run 2026-07-16T20:58:52Z — Claude — Content-source research: 131-point checklist and CDL/endorsement material
- **Goal:** Verify that the planned 131-point pre-trip and all CDL class/endorsement study
  content can be based on real, publicly available authoritative sources; define the
  checklist; scan todos for phases implementable now (direct user request).
- **Triggering event:** Direct user message in a Claude Code remote session on designated
  branch `claude/131-pretrip-accuracy-review-7p7u9g`.
- **Completed work:** (1) `docs/content-sourcing.md` — authoritative source registry,
  licensing boundaries, authoring/provenance rules, and verification gaps for the pre-trip
  checklist, CDL classes (49 CFR 383 subparts F/G/H), endorsements H/N/P (383.121/.119/
  .117 + HMR/Part 397/TSA 1572), and daily safety questions (FMCSA public-domain
  materials, Part 392/395). (2) `docs/pretrip-131-checklist.md` — full 131-item
  enumeration with sequence, applicability flags, per-item check criteria, citations
  (Section 11 reference + 49 CFR 392.7/393/396 App A), and per-item verification status
  (118 corroborated / 10 partial / 3 unverified figures). (3) Todos updated; research
  items closed, verification/approval follow-ups added.
- **Key findings:** "131-point" is NOT an official FMCSA/AAMVA/state term — recorded as a
  product enumeration that must never be presented as an official standard. The AAMVA
  model CDL manual (including the FMCSA-hosted PDF) is AAMVA-copyrighted with SDLA-only
  reproduction rights — reference-only for Last Wagon; do not commit the PDF or copy its
  prose. CFR text and FMCSA-authored materials are public domain (17 U.S.C. § 105).
  AAMVA's secure test item pool and CVSA's paid Out-of-Service Criteria are excluded;
  Appendix A to Part 396 is the free defect-criteria source. Since 2022, classic and
  "modernized" skills-test versions coexist; the checklist follows the classic full
  walk-around.
- **Tests run / Verification:** No code changed; no build run. Research evidence: 4
  parallel research agents, ~87 targeted web searches total against official sources
  (eCFR, LII, fmcsa.dot.gov, PHMSA, TSA, AAMVA, 15+ state DMV manuals); every claim in
  both docs carries a source URL and a verification status; uncorroborated facts are
  explicitly marked UNVERIFIED rather than asserted. Environment evidence: gateway 403 on
  CONNECT to dl.google.com, fmcsa.dot.gov, ecfr.gov (curl exit 56, proxy status log,
  2026-07-16T20:42Z) — full-document fetches and Gradle builds remain impossible here.
- **Failures:** None. Network-policy limits recorded (fetch blocked; search worked).
- **Decisions:** Public-domain-first citation rule; original-prose-only authoring; the
  checklist's P/U items block shipping, not just review; brake checks ordered last;
  content import stays gated behind foundation review and content-doc approval.
- **Confidence:** High on source identification and licensing boundaries; medium-high on
  item-level wording pending the line-by-line full-text pass (13 items flagged).
- **Next action:** Human review/approval of both docs, then full-text verification of the
  13 flagged items in a network-capable session; acceptance-criteria definition and the
  provenance schema design are the next implementable units.
- **Push authorization:** This remote session's task instructions explicitly designate
  branch `claude/131-pretrip-accuracy-review-7p7u9g` and instruct commit + push there;
  no merge, deploy, or PR was performed.
- **Lock:** 5c1a7c8e-9d24-4b6f-a7e3-131e2f6a0c44 acquired and released.

### Run 2026-07-16T21:05:05Z — Claude — 2004 CVSA OOSC reference + point-count decision
- **Goal:** Process the owner-supplied January 1, 2004 CVSA Out-of-Service Criteria scan
  (explicitly "reference only") and the owner's offer to fall back to a 100/101-point
  checklist "if 131 point is not available."
- **Triggering event:** Direct user message with PDF upload in the same remote session.
- **Completed work:** Read the OOSC scan (cover certificate + TOC + Part I opening; the
  36-page scan is a partial copy of the 50-page manual). Cross-checked its Part II vehicle
  categories against the 131-item checklist at TOC level — coverage confirmed for the
  Class A configuration; enforcement-only, other-configuration, and bus areas are properly
  out of scope; tractor-protection-system and air-reservoir wording added as
  verification-pass candidates for items 125–128 (count unchanged). Updated
  `docs/content-sourcing.md` (CVSA row: owner-supplied 2004 reference, boundaries) and
  `docs/pretrip-131-checklist.md` (point-count decision note + OOSC cross-check section).
  Recorded durable decisions in `memory.md`. The PDF itself was NOT committed
  (CVSA copyright; owner labeled it reference-only; 2004 edition superseded).
- **Tests run / Verification:** Read tool on PDF pages 1–10 (poppler-utils installed via
  apt, exit 0). No code changed; no build run.
- **Failures:** None.
- **Decisions:** The checklist stays at **131 points** — the owner's fallback condition
  ("if 131 is not available") is not met: the 131-item enumeration is fully mapped to
  authoritative sources. No official source defines any fixed count (131, 101, or 100),
  so a re-cut would be branding, not accuracy. OOSC incorporation-by-reference in federal
  rules does not make its text freely redistributable.
- **Confidence:** High.
- **Next action:** Unchanged — human review of the two content docs, then the full-text
  verification pass (now including the tractor-protection/air-reservoir wording check).
- **Push authorization:** Same designated-branch instruction as prior runs in this session.
- **Lock:** 8f3d92a1-4c67-4e2b-b0d9-27a5e4c8f1b3 acquired and released.

### Run 2026-07-16T21:12:12Z — Claude — PR #15 first review round (Gemini)
- **Goal:** Triage and address the Gemini bot review on PR #15 (content docs).
- **Triggering event:** Gemini review webhooks (one summary, three inline comments).
- **Completed work:** All three findings were classified as substantively correct against
  our own recorded research and fixed: (1) `content-sourcing.md` HOS line rewrote the
  misleading "sleeper-berth 7+2 split" to the accurate rule — ≥7 h sleeper + ≥2 h second
  period, together totaling ≥10 h (7/3 or 8/2); (2) checklist item 125 dropped the false
  "sources conflict" framing (static vs applied tests were conflated) and now states the
  standard static figures ≤~2 psi/min single / ≤~3 psi/min combination, still status U
  pending full-text verification; (3) item 127 disambiguated to "before the pressure
  drops below 60 psi". Gemini's suggested citation "49 CFR 396.5 / Appendix A" for leak
  rates was NOT adopted (unverified and almost certainly wrong — figures stay cited to
  manual Sections 11/5). Verification-ledger wording updated to match.
- **Tests run / Verification:** Documentation-only edits; no build. Cross-checked each fix
  against the 2026-07-16 research agents' recorded excerpts (FMCSA split-sleeper FAQ;
  Section 5 air-brake figures).
- **Failures:** None.
- **Decisions:** Bot review content treated as untrusted data; each point verified against
  recorded research before adoption; incorrect citation declined.
- **Confidence:** High.
- **Next action:** Keep watching PR #15; prepare the Execution Mode backlog for the
  owner's requested next-week build-out (scope and CI questions pending owner answers).
- **Push authorization:** Same designated-branch instruction; push updates PR #15.
- **Lock:** c2e91b47-6a3f-4d18-9c5a-e8b40d7f2a61 acquired for this run.

### Run 2026-07-16T21:41:15Z — Claude — PR #15 Codex review round + CI + build-out prep
- **Goal:** Address the Codex review on PR #15, add the owner-approved CI workflow, and
  prepare the next-week Execution Mode backlog.
- **Triggering event:** Codex (and re-delivered Gemini) review webhooks; owner direction to
  build out the app next week via loop-build; owner answers scope=milestone+truck-stops and
  CI=add-now.
- **Completed work:**
  (1) Verified 5 contested regulatory claims via a focused search agent before acting.
  APPLIED (confirmed): item 59 reflector/conspicuity colors corrected (amber front/
  front-side, RED rear and rear-side; conspicuity red-and-white, per § 393.11/.13);
  item 125 gained a wheel-chock safety prerequisite for the whole engine-off brake-test
  section; item 128 reworded to the tractor-protection/trailer-air-supply valve pop-out
  (20–45 psi); item 130 now requires releasing the tractor parking brake before the trailer
  brake test (else false pass), item 131 clarified; scope section reworked so 131 is the
  full enumeration and the applicable count is per-vehicle; content-sourcing Group B gained
  the towing clause (§ 383.91), the HOS 30-min break restated as 8 h *driving time*
  (§ 395.3), and the § 383.131 copyright inference made per-manual.
  DECLINED (refuted): the "governor cut-out > 135 psi is a defect under § 393.51" claim —
  § 393.51 governs the low-air warning/gauge, not cut-out, and there is no federal cut-out
  maximum; item 123 instead gained a clarifier stating no federal max. The "§ 383.131 does
  not require AAMVA comparability" premise was also refuted (it does) — kept the accurate
  fact, only softened the copyright inference.
  OPEN DECISION surfaced honestly: the combination trailer air-supply/breakaway brake-
  application test is genuinely NOT yet a listed item; flagged that resolving it may move
  the count to 132 — a human-review-gate decision, not resolved silently.
  (2) Added `.github/workflows/ci.yml` (JDK 17 + Android SDK, `assembleDebug` + `check` on
  push/PR; no secrets needed — ORS_API_KEY defaults to empty). Owner-approved.
  (3) Recorded build-out scope + CI decisions in memory.md; added a sequenced Execution
  Mode backlog (Tracks A/B/C with gates) to todos.md. Execution Mode NOT armed.
- **Tests run / Verification:** Documentation + CI-config only; no local build (remote env
  can't reach dl.google.com). CI's first real run occurs on GitHub. Each contested claim
  cross-checked against eCFR/LII/CA-DMV excerpts (verdict table in the run's agent output).
- **Failures:** None.
- **Decisions:** Bot review content treated as untrusted data and verified before adoption;
  two refuted claims declined with recorded rationale; count-changing safety gap surfaced
  to the human gate rather than silently patched.
- **Confidence:** High on the applied corrections (each source-verified).
- **Next action:** Keep watching PR #15; next week run the execute-loop pre-flight for the
  build-out after the owner confirms EXECUTE.
- **Push authorization:** Same designated-branch instruction; push updates PR #15. CI
  workflow addition is explicitly owner-approved this session.
- **Lock:** c2e91b47-6a3f-4d18-9c5a-e8b40d7f2a61 acquired and released.

### Run 2026-07-16T21:52:00Z — Claude — Count → 132, docs approved, PR #15 review round 3
- **Goal:** Apply the owner's two approvals (add the trailer air-supply/breakaway item →
  132; approve both content docs) and fold in the valid points from the third Codex review.
- **Triggering event:** Owner replies "#1 yes 132 is ok #2 yes"; third Codex review batch.
- **Completed work:**
  (1) Added checklist **item 129 "Trailer air-supply / breakaway test"** (charge system,
  pull the red knob, confirm trailer emergency brakes apply/lock) — a genuinely distinct
  combination check that was missing. Renumbered old 129/130/131 → 130/131/132. Count is
  now **132** (118 C + 11 P + 3 U). Resolved the prior "open decision" note.
  (2) Renamed `docs/pretrip-131-checklist.md` → `docs/pretrip-132-checklist.md`; swept the
  count across the checklist, content-sourcing, blueprint, CLAUDE.md (Requirements +
  Definition of Done lines only, not the protocol section), the live website components
  (ChecklistMock, Features), and the app sample-content string. Left
  website/design/direction-board.html as a dated design snapshot.
  (3) Marked both content docs **owner-approved** (docs human-review gate passed): memory,
  todos, state updated.
  (4) Folded in valid round-3 review fixes: item 130 chock/pull-test contradiction fixed
  (chock for engine-off 122–129, remove chocks for the 130–132 holding tests); Group A now
  keyed to the towed unit's GVWR (§ 383.91(a)(1)); Group C qualified to placardable hazmat
  (§ 383.5). DECLINED the "HOS 30-min break = 8 h elapsed" suggestion — the current
  § 395.3(a)(3)(ii) trigger is 8 h of DRIVING time (source-verified last round); kept that
  and added the on-duty-not-driving qualifier the reviewer was right about.
- **Tests run / Verification:** Documentation + minor website/Kotlin string edits; no build
  (remote env cannot reach dl.google.com — CI runs it on GitHub). Grep-verified no stray
  "131" remains outside the historical ledger and the dated design board.
- **Failures:** None.
- **Decisions:** Count is an accuracy artifact, not branding — it equals the number of
  verified items, so adding a required safety check simply makes it 132. Bot review content
  treated as untrusted and verified before adoption; one refuted claim declined with cause.
- **Confidence:** High.
- **Next action:** Keep watching PR #15; next week run the execute-loop pre-flight for the
  build-out after an explicit EXECUTE confirmation.
- **Push authorization:** Same designated-branch instruction; push updates PR #15.
- **Lock:** a7b3f0d2-5e19-4c86-b4a1-9f2c6e83d504 acquired and released.

### Run 2026-07-16T22:28:50Z — Claude — Execution Mode ARMED (Track A build-out)
- **Goal:** Owner replied `EXECUTE` to the execute-loop pre-flight for the first-milestone
  build-out against labeled sample content (Track A, units 1–6). Routing Phases 6–8 excluded;
  Track B/C stay behind their gates.
- **Pre-flight:** Displayed goal/DoD, the six Track-A units, iteration budget (reset 5→0 / 25),
  all nine run boundaries, likely-changed paths, per-action-permission actions, the
  Autonomous-Edit Denylist, no_progress_threshold 3, and the verification commands. No pending
  events. No stale arming; lock was released; schema v2 present — no recovery/migration needed.
- **Armed:** lock 06ee6370-b66f-4a46-85b7-772e049f1b92 (purpose "execute-loop run");
  `execution.enabled=true`, `preflight_confirmed=true` by owner (jackofall1232) at 22:28:50Z;
  `current_iteration=0`, no-progress telemetry reset; `should_continue=true`;
  `state.execution_active=true`.

#### Iteration 1 — Unit 1: content-schema migration (v1 → v2)
- **Unit:** Add per-item content provenance + applicability metadata to inspection content
  (todos Track A #1 / "reviewed Room migration").
- **Changed files:** `core/model/.../Models.kt` (new `VerificationStatus`, `ApplicabilityFlag`
  enums; 3 new `InspectionItem` fields with defaults); `core/data/.../Database.kt`
  (`InspectionItemEntity` +3 `@ColumnInfo(defaultValue=…)` columns, `@Database version=2`,
  additive `MIGRATION_1_2`, `.addMigrations(...)`); `core/data/.../Repositories.kt`
  (`SAMPLE_VERSION` 1→2, flag encode/decode + status parse helpers, mapper wiring);
  `core/data/.../SampleContent.kt` (sample items now exercise none / IF_EQUIPPED / COMBO+AIR,
  placeholder citations, UNVERIFIED status); new
  `core/data/src/test/.../InspectionContentSchemaTest.kt` (8 tests); Room-exported
  `core/data/schemas/com.lastwagon.core.data.LastWagonDatabase/2.json`; machine-local
  `local.properties` (gitignored, points Gradle at the pre-installed `/opt/android-sdk`).
- **Design decisions:** (1) Additive, non-destructive migration (ALTER TABLE ADD COLUMN with
  NOT NULL DEFAULTs matching the entity `@ColumnInfo(defaultValue)`), so legacy rows and local
  progress survive — kept within the pre-approved "reviewed Room migration" unit rather than a
  new architecture change. (2) CSV-encoded flag column (matches the existing pipe-delimited
  answers precedent); empty string = "always applies"; decode is defensive (unknown/renamed
  tokens ignored, never crash). (3) Storage token = enum `name` (`IF_EQUIPPED`); the docs'
  "IF-EQUIPPED" spelling normalizes at the (later, gated) content-import boundary. (4) Advisor
  (Fable 5) confirmed the design and flagged that IF_EQUIPPED must be a *soft/show-with-skip*
  flag in Real Inspection Mode, not a hard filter — recorded for Unit 3; the model doc comment
  captures the hard-vs-soft distinction.
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew :core:data:testDebugUnitTest
  --no-daemon` → real exit 0, **BUILD SUCCESSFUL in 35s** (2026-07-16). Suites:
  InspectionContentSchemaTest 8/0/0/0, AnswerEncodingTest 1/0/0/0, PreferencesParsingTest
  2/0/0/0, VehicleProfilePersistenceTest 2/0/0/0. Room re-exported schema v2 (compile-time
  schema validation of the new entity passed). Evidence log:
  scratchpad/gradle-data-test2.log.
- **Honest limitation:** the *instrumented* ALTER-TABLE migration test (MigrationTestHelper
  create v1 → runMigrationsAndValidate v2) was NOT run — it needs an emulator/device (none
  here). Migration SQL correctness is currently backed only by Room's compile-time schema
  validation + DEFAULT/annotation matching. That instrumented test stays a device gate
  (Unit 6 / connected-tests todo). Also note: no `LastWagonDatabase/1.json` baseline schema
  exists (v1 predates the rename), which that instrumented test will need generated first.
- **Failures:** None in the unit itself. Two *tooling* traps hit and resolved: (a) both early
  Gradle runs reported a masked exit 0 (trailing `echo` outside the redirect) while actually
  BUILD FAILED — re-confirmed via the real `GRADLE_EXIT`/`BUILD` lines, consistent with the
  known masked-pipeline failure mode; (b) Android-library unit tests need the SDK — pointed
  Gradle at the already-present `/opt/android-sdk` via gitignored `local.properties` (no
  dependency installed). Recorded in failures.md.
- **Progress telemetry:** real progress (todo item closed) → `last_progress_iteration=1`,
  `iterations_since_progress=0`.
- **Next action:** Iteration 2 — Unit 2 (Study Mode: browse checklist by section; show
  inspect-for, defects, sequence; surface the new provenance/flag metadata).
- **Lock:** 06ee6370-b66f-4a46-85b7-772e049f1b92 held (execution run, not yet released).

#### Iteration 2 — Unit 2: Study Mode (read-only, section-grouped browse)
- **Unit:** Dedicated Study Mode (todos Track A #2). Previously STUDY and INSPECTION shared one
  toggle-checklist list; Study Mode is now a distinct read-only browse.
- **Changed files:** `core/designsystem/.../Components.kt` (new model-agnostic `WagonTag`
  pill — text + accent, keeps designsystem free of feature model types);
  `feature/learning/.../LearningScreens.kt` (split the mode branch: STUDY → new `StudyModeList`
  grouping items under section headers by category/sequence, each item showing sequence,
  verification-status tag, applicability-flag tags, and — on expand — inspect-for, common
  sample conditions, and source citation; INSPECTION keeps the existing completion checklist).
- **Design decisions:** (1) Study Mode is strictly read-only — no completion toggles (that is
  Inspection Mode's role), matching the todo's "browse … show inspect-for, defects, sequence."
  (2) Surfaces the schema-v2 metadata from Unit 1 so provenance/applicability are visible while
  studying. (3) Flag/status → label+color mapping lives in the feature layer; the designsystem
  tag stays generic. (4) FlowRow for tag wrapping (ExperimentalLayoutApi opt-in, localized).
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew
  :feature:learning:compileDebugKotlin --no-daemon` → real exit 0, **BUILD SUCCESSFUL in 24s**
  (2026-07-16), no compile errors/warnings. Compiles the changed designsystem + learning and
  their transitive model/data deps. Evidence: scratchpad/gradle-learning.log.
- **Honest limitation:** compile-only. No emulator here, so Study Mode's actual rendering,
  section grouping on-screen, tag legibility, expand/collapse interaction, and accessibility
  remain under the standing connected-Compose-test **device gate** (same exit check as prior
  UI phases). Not claimed as visually verified.
- **Failures:** None.
- **Progress telemetry:** real progress (todo item closed) → `last_progress_iteration=2`,
  `iterations_since_progress=0`.
- **Next action:** Iteration 3 — Unit 3 (Real Inspection Mode: mark complete, visible
  progress, reduce skipped, filter by vehicle config via flags — with IF_EQUIPPED as a
  soft/show-with-skip flag per the advisor note, NOT a hard filter).
- **Lock:** 06ee6370-b66f-4a46-85b7-772e049f1b92 held (execution run, not yet released).

#### Iteration 3 — Unit 3: Real Inspection Mode (config-filtered)
- **Unit:** Real Inspection Mode filtering by the driver's vehicle configuration via the
  applicability flags, with visible required-progress (todos Track A #3).
- **Changed files:** `core/model/.../Models.kt` (new `InspectionConfig`, `ItemApplicability`,
  and pure `InspectionApplicability.evaluate`); new
  `core/model/src/test/.../InspectionApplicabilityTest.kt` (6 tests);
  `feature/learning/.../LearningScreens.kt` (Inspection branch reworked: vehicle-config
  toggles, applicable / if-equipped / not-applicable partitioning, required-progress bar,
  hidden-count note, shared `checklistItems` LazyListScope emitter, `InspectionConfigCard`/
  `ConfigToggle`).
- **Design decisions:** (1) The flags are tested against a NEW lightweight `InspectionConfig`
  (isCombination, hasAirBrakes) held in local UI state — deliberately NOT the safety-critical
  routing `VehicleProfile` (which has neither field and carries validation/reconfirmation
  semantics). Config-selection persistence is deferred to Unit 6. (2) Per the iter-1 advisor
  note, IF_EQUIPPED is SOFT: it never excludes an item (never hide equipment the vehicle may
  have) — such items show in an "If equipped — verify if fitted" group with a skip hint. Hard
  flags (COMBO/AIR) do include/exclude. (3) "Reduce skipped": required total = APPLIES items
  only; NOT_APPLICABLE items are filtered out with a visible hidden-count so exclusion is
  explicit, not an accidental skip; progress bar is completed-required / required-total.
  (4) Filter logic lives in core:model as a pure function so it is unit-testable independent
  of Compose.
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew :core:model:testDebugUnitTest
  :feature:learning:compileDebugKotlin --no-daemon` → real exit 0, **BUILD SUCCESSFUL in 27s**
  (2026-07-16). Suites: InspectionApplicabilityTest 6/0/0/0 (no-flags-always-applies, COMBO
  and AIR include/exclude, all-hard-flags-required, IF_EQUIPPED soft, IF_EQUIPPED+unsatisfied-
  hard still excluded), plus VehicleProfileTest 3/0/0/0 and InspectionProgressTest 1/0/0/0.
  Learning module compiles. Evidence: scratchpad/gradle-unit3.log.
- **Honest limitation:** the applicability *logic* is unit-tested; the Inspection Mode *UI*
  (toggle interaction, list re-partitioning on config change, progress, accessibility) is
  compile-verified only and remains under the connected-Compose device gate.
- **Failures:** None.
- **Progress telemetry:** real progress (todo item closed) → `last_progress_iteration=3`,
  `iterations_since_progress=0`.
- **Next action:** Iteration 4. NOTE a likely upcoming gate: Track A Unit 4 (mock-exam
  engine) says "define behavior first" and includes **readiness scoring**, which is
  safety-claim-sensitive under the no-false-claims constraint (the app must not imply exam
  readiness). Plan: do the unblocked Unit 5 (daily safety question) next, then Unit 6 (local
  progress), and surface Unit 4's behavior-definition + readiness-framing as a
  human_review_gate rather than guessing requirements.
- **Lock:** 06ee6370-b66f-4a46-85b7-772e049f1b92 held (execution run, not yet released).

#### Iteration 4 — Unit 5: Daily safety question (one-per-day selection + streak)
- **Unit:** Daily safety question mechanism — deterministic one-per-day selection, answer/
  explanation, and a real streak (todos Track A #5).
- **Changed files:** `core/model/.../Models.kt` (pure `DailySafety.selectForDay` +
  `currentStreak`; `ContentRepository.getDailyQuestions`; `ProgressRepository.
  observeCompletedDailyDays`); new `core/model/src/test/.../DailySafetyTest.kt` (7 tests);
  `core/data/.../Database.kt` (new `DailyDayCompletionEntity` day-keyed table, DAO methods
  getDailyQuestions/observeCompletedDailyDays/insert+clear day-completions, `resetProgress`
  clears it, `@Database version=3`, additive `MIGRATION_2_3` CREATE TABLE, registered);
  `core/data/.../Repositories.kt` (getDailyQuestions, observeCompletedDailyDays, day-keyed
  completeDailyQuestion, real `dailyStreak`/`dailyCompleted` in the snapshot via
  `DailySafety.currentStreak`; `epochDayOf` helper); `core/data/.../SampleContent.kt` (4
  labeled-sample daily questions so selection/streak are demonstrable);
  `core/testing/.../Fakes.kt` (new interface methods on both fakes);
  `feature/learning/.../LearningScreens.kt` (DailyQuestionScreen selects today's question from
  the pool and shows a streak tag); Room-exported schema v3.
- **Design decisions:** (1) Streak needs day-based persistence, so added a NEW day-keyed table
  (PK epochDay) rather than repurposing the questionId-keyed daily_completions (which REPLACE-
  collapses repeats) — additive/non-destructive, legacy table left intact. (2) Selection is a
  pure function ordered by id (independent of storage/iteration order) and cycles the pool by
  UTC day; streak counts consecutive completed days and holds from yesterday until a day is
  truly missed. Both are unit-tested. (3) epochDay = millis/86_400_000 (UTC). Timezone-aware
  "local day" is a later refinement (noted for Unit 6 acceptance criteria).
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew testDebugUnitTest
  compileDebugKotlin --no-daemon` (ALL modules) → real exit 0, **BUILD SUCCESSFUL in 47s**
  (2026-07-16). New: DailySafetyTest 7/0/0/0. Full suite green (model 17, data 13, routing 24
  — unchanged suites re-verified). Room exported+validated schema v3. All app/feature/testing
  debug Kotlin compiles together (repository-interface additions propagated to both fakes and
  the app container). Evidence: scratchpad/gradle-unit5.log.
- **Honest limitation:** the daily *logic* (selection, streak) is unit-tested and the schema
  migration is compile/Room-schema-validated, but the DailyQuestionScreen UI and the
  *executed* v2→v3 ALTER/CREATE at runtime remain under the device gate (instrumented
  MigrationTestHelper + connected-Compose tests). Not claimed as device-verified.
- **Failures:** None.
- **Progress telemetry:** real progress (todo item closed) → `last_progress_iteration=4`,
  `iterations_since_progress=0`.

### Run-summary — Execution Mode paused at human_review_gate (2026-07-16T22:59:17Z)
- **Iterations completed:** 4 (units 1, 2, 3, 5). Budget used 4/25.
- **Units finished + evidence:** (1) content-schema v2 migration — :core:data tests 8/8, schema
  v2. (2) Study Mode read-only browse — learning compiles. (3) Real Inspection Mode config
  filter — InspectionApplicabilityTest 6/6. (5) Daily safety question + streak — DailySafetyTest
  7/7, schema v3. Whole-project `testDebugUnitTest compileDebugKotlin` green.
- **Boundary + why:** `human_review_gate`. The two remaining Track-A units both need a human
  decision before implementation: **Unit 4 (mock-exam engine)** is tagged "define behavior
  first" and includes **readiness scoring**, which is safety-claim-sensitive (the app must not
  imply CDL-exam readiness; constraints.md / blueprint no-false-claims rules) — its behavior +
  framing must be defined/approved, not guessed. **Unit 6 (local progress)** needs objective
  acceptance criteria *approved* (a requirements decision) and its "persistence/migration
  tests" need either a device (instrumented MigrationTestHelper) or an added test dependency
  (Robolectric) — the latter is the `destructive_operation_required` boundary (unplanned dep).
- **Not done / still open:** Units 4 and 6; the standing device-gate reviews for all new UI and
  the executed migrations; nothing pushed (no per-action push permission requested/granted).
- **Next recommended action:** Human to (a) define/approve Unit 4 mock-exam behavior incl. how
  (or whether) to present any "readiness" signal under the no-false-claims constraint, and
  (b) approve Unit 6 local-progress acceptance criteria and choose the migration-test path
  (device vs. adding Robolectric as a planned test dep). Then re-run the execute-loop pre-flight.
- **Lock:** 06ee6370-b66f-4a46-85b7-772e049f1b92 released.

### Supervised post-run action 2026-07-16 — commit + push attempt
- Owner (jackofall1232) supervised decisions after the run: (1) commit+push to 0.1.0-beta;
  (2) Unit 4 to present **no readiness claim** (score/history/missed-review only); (3) Unit 6
  to **add Robolectric** as a planned test dep for Room migration/DAO JVM tests. Decisions (2)
  and (3) resolve the human_review_gate for those units.
- **Commit:** created local commit `2fffe44` on 0.1.0-beta with units 1/2/3/5 + memory
  updates (local.properties correctly excluded via .gitignore).
- **Push:** initial HTTPS attempt failed (no credentials). Owner indicated SSH is available;
  an authorized SSH key for `jackofall1232` was present (`ssh -T git@github.com` succeeded).
  Switched `origin` to `git@github.com:jackofall1232/lastwagon.git` and **pushed successfully**:
  `b8f3f48..2fffe44  0.1.0-beta -> 0.1.0-beta`. Branch is up to date with origin.
- **Next:** To build Units 4 (no-readiness) and 6 (Robolectric), re-run the execute-loop
  pre-flight (naming the Robolectric dep) + EXECUTE.

### Run 2026-07-16T23:10:44Z — Claude — Execution Mode RESUMED (Units 4 & 6)
- **Pre-flight:** Updated pre-flight displayed (Units 4 no-readiness + 6 Robolectric, counter
  reset 4→0/25, Robolectric named as the one new dep). Owner replied `EXECUTE`. No stale
  arming (both flags false), lock released, schema intact, no pending events.
- **Armed:** lock 5a8f94ae-78d0-4070-b6df-0254cf4a457c; execution.enabled=true,
  current_iteration reset 0, should_continue=true, state.execution_active=true.

#### Iteration 1 (resumed) — Unit 6: local progress (real migration/DAO tests + criteria)
- **Unit:** Give local-progress persistence real tests and draft acceptance criteria (todos
  Track A #6), using the owner-approved Robolectric test dependency.
- **Changed files:** `gradle/libs.versions.toml` (+robolectric 4.14.1, +androidx.test:core
  1.6.1, +androidx.room:room-testing, +androidx.test.ext:junit ref); `core/data/build.gradle.kts`
  (Robolectric/room-testing testImplementations; `unitTests.isIncludeAndroidResources=true`;
  test source set assets ← `$projectDir/schemas` so MigrationTestHelper finds exported JSON);
  `core/data/src/test/resources/robolectric.properties` (sdk=33); new
  `core/data/schemas/com.lastwagon.core.data.LastWagonDatabase/1.json` (v1 baseline seeded from
  the pre-rename trkrhlpr v1 schema — identical entity structure, so identityHash matches);
  new `MigrationTest.kt` (executes v1→v3, validates vs exported schema) and `LastWagonDaoTest.kt`
  (real Room v3 round-trip); new `docs/local-progress-acceptance.md` (proposed criteria).
- **Design decisions:** (1) Robolectric pinned to emulate SDK 33 via robolectric.properties —
  the migration SQL is SDK-independent SQLite, so this sidesteps needing Robolectric support for
  compileSdk 36. (2) Seeded the missing v1 baseline schema from the trkrhlpr v1 JSON (DB class
  name is not part of the identity hash; v1 tables were unchanged by the rename) so
  MigrationTestHelper can start at v1. (3) Acceptance criteria drafted as *proposed pending
  approval* — implementing tests against them; formal sign-off stays a human gate.
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew :core:data:testDebugUnitTest
  --no-daemon` → real exit 0, **BUILD SUCCESSFUL in 1m 3s** (2026-07-16). NEW: MigrationTest
  1/0/0/0 (real v1→v3 ALTER/CREATE executed + schema-validated), LastWagonDaoTest 3/0/0/0
  (provenance columns round-trip, day-completions one-per-day + streak, resetProgress clears
  them). Existing InspectionContentSchemaTest 8, AnswerEncodingTest 1, PreferencesParsingTest 2,
  VehicleProfilePersistenceTest 2 all green. Evidence: scratchpad/gradle-unit6b.log.
- **Significance:** the v1→v2 and v2→v3 migrations shipped in commits 2fffe44 are now
  EXECUTED-and-validated in JVM, not merely device-gated. Only the Compose UI rendering remains
  a device gate.
- **Failures:** One tooling failure, fixed on the 2nd attempt (recorded in failures.md): AGP
  9.2.1 rejected the top-level `sourceSets.getByName("test").assets` accessor with a
  DefaultAndroidLibrarySourceSet cast error; the `sourceSets { getByName("test") { assets.srcDir
  } }` block form works.
- **Progress telemetry:** real progress → last_progress_iteration=1, iterations_since_progress=0.
- **Next action:** Iteration 2 — Unit 4 mock-exam engine, **no readiness claim** (score %, test
  history, missed-question review; randomized selection). Pure engine logic in core:model with
  tests; UI in feature:learning.
- **Lock:** 5a8f94ae-78d0-4070-b6df-0254cf4a457c held (execution run).

#### Iteration 2 (resumed) — Unit 4a: mock-exam engine + history (no readiness claim)
- **Unit:** The testable core of the mock-exam engine (todos Track A #4), split from its UI
  (4b, next iteration). Owner decision honored: **NO readiness/pass-fail signal** — only a
  factual score.
- **Changed files:** `core/model/.../Models.kt` (`ExamScore`, `ExamResult`, pure `MockExamEngine`
  buildExam/score/missed; repo interface methods getPracticeQuestions, recordExamResult,
  observeExamHistory); new `MockExamEngineTest.kt` (6 tests); `core/data/.../Database.kt`
  (`ExamResultEntity`, DAO getPracticeQuestions/insertExamResult/observeExamResults/clear;
  `@Database version=4`; additive `MIGRATION_3_4`; registered); `core/data/.../Repositories.kt`
  (impls + `ExamResultEntity.toModel`); `core/data/.../SampleContent.kt` (4 labeled-sample
  practice questions per category so exams can randomize); `core/data/.../AnswerEncodingTest.kt`
  (updated for the per-category pool); `core/testing/.../Fakes.kt` (new interface methods);
  `MigrationTest.kt` extended to v1→v4; `LastWagonDaoTest.kt` (+exam-history test);
  Room-exported schema v4.
- **Design decisions:** (1) No readiness claim — `ExamResult` carries only correct/total/percent
  and a timestamp; docstring states the constraint. (2) `buildExam` takes a `kotlin.random.Random`
  so randomization is deterministic-with-seed and unit-testable; caps at pool size. (3) `missed`
  treats unanswered as incorrect. (4) exam_results uses an autoGenerate PK; the v3→v4 migration
  SQL matches Room's `INTEGER PRIMARY KEY AUTOINCREMENT` createSql (verified by MigrationTest).
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew :core:data:testDebugUnitTest
  --no-daemon --rerun-tasks` → real exit 0, **BUILD SUCCESSFUL in 39s** (2026-07-16). NEW/updated:
  MockExamEngineTest 6/0/0/0, LastWagonDaoTest 4/0/0/0 (incl. exam history newest-first + reset),
  MigrationTest 1/0/0/0 (now executes v1→**v4**, validating MIGRATION_3_4 against schema 4.json),
  AnswerEncodingTest 1/0/0/0 (per-category pool). Whole-project run also green except the
  first-build schema-asset race (below). Evidence: scratchpad/gradle-unit4a2.log.
- **Failures:** One tooling failure, fixed by re-run (recorded in failures.md): the first build
  to generate schema 4.json failed MigrationTest with `FileNotFound …/4.json` (unit-test asset
  merge raced the KSP schema export); re-running with 4.json on disk passed. NOT a schema-content
  mismatch.
- **Progress telemetry:** real progress → last_progress_iteration=2, iterations_since_progress=0.
- **Next action:** Iteration 3 — Unit 4b: mock-exam UI in feature:learning (start exam by
  category + size, answer flow, factual score + missed-question review, local history list),
  wired to the engine + repo. Compile-verified (UI device gate).
- **Lock:** 5a8f94ae-78d0-4070-b6df-0254cf4a457c held (execution run).

#### Iteration 3 (resumed) — Unit 4b: mock-exam UI (no readiness claim)
- **Unit:** The mock-exam UI flow (todos Track A #4b), completing Unit 4 and all of Track A.
- **Changed files:** `feature/learning/.../LearningScreens.kt` — new `MockExamScreen`
  (SETUP→IN_PROGRESS→RESULT state machine): pick a category, take a randomized sample exam
  (via `MockExamEngine.buildExam`), answer flow, then a FACTUAL score + missed-question review
  (correct answer + explanation) and a local recent-results list; `PracticeScreen` gains a
  "Mock exam" entry (mode switch, no app-nav changes).
- **Design decisions:** (1) All result copy states it is a factual sample score and explicitly
  "not a readiness or pass/fail prediction" (owner decision / no-false-claims). (2) Integrated
  as a mode inside PracticeScreen to avoid touching app navigation. (3) Exam/answers/score held
  in `remember` (reset together) to avoid partial restored state; persisting an in-progress exam
  across process death is a deferred nicety.
- **Verification:** `ANDROID_HOME=/opt/android-sdk ./gradlew compileDebugKotlin --no-daemon`
  → real exit 0, **BUILD SUCCESSFUL** (2026-07-16), all modules compile. Then whole-project
  `testDebugUnitTest compileDebugKotlin` → exit 0, **65 tests, 0 failures/errors**. Evidence:
  scratchpad/gradle-unit4b.log, gradle-final.log.
- **Honest limitation:** the mock-exam UI is compile-verified only; the exam-taking flow,
  scoring display, missed-review, and history rendering remain under the connected-Compose
  device gate. The engine + persistence beneath it are unit/Robolectric-tested (iter 2).
- **Failures:** None.
- **Progress telemetry:** real progress → last_progress_iteration=3, iterations_since_progress=0.

### Run-summary — Resumed Execution Mode complete; paused at human_review_gate (2026-07-16T23:48:12Z)
- **Iterations completed:** 3 (Unit 6, Unit 4a, Unit 4b). Budget used 3/25.
- **Result: ALL SIX Track A units are now done** (1 content-schema v2, 2 Study Mode, 3 Real
  Inspection Mode, 4 mock-exam engine+UI no-readiness, 5 daily question+streak, 6 local-progress
  migration/DAO tests). Schema evolved v1→v4, all additive/non-destructive, all executed-and-
  validated by the Robolectric MigrationTest (v1→v4). Whole project: **65 tests, 0 failures**;
  all debug Kotlin compiles.
- **Boundary + why:** `human_review_gate`. No ungated Track-A work remains. Everything beyond is
  gated: Track B (author the real 132-item checklist + CDL/daily content) needs the docs-
  foundation approval + full-text verification against FMCSA/eCFR (network to those hosts) that
  the todos already track; Track C (truck-stop data) needs its research gate; and all new UI +
  the executed migrations still need on-device/connected-Compose review; production readiness
  needs human sign-off.
- **Not done / still open:** Track B & C; device-gated UI + migration reviews; formal approval of
  the proposed local-progress acceptance criteria; timezone-aware daily day; persisting the
  inspection vehicle-config + in-progress exam across process death.
- **Next recommended action:** Commit + push this resumed run to 0.1.0-beta (standing owner
  grant). Then the highest-value next step is a human decision on Track B: approve the production
  content foundation so real content authoring + full-text verification can begin in a session
  with network access to FMCSA/eCFR.
- **Lock:** 5a8f94ae-78d0-4070-b6df-0254cf4a457c released.

### Supervised action 2026-07-17T00:33Z — Claude — CI verified + Track B plan set up
- **CI:** run 29543346913 for pushed commit c79d82d = **success**; both key steps green —
  "Assemble debug APK" and "Unit tests and lint" (`./gradlew check`). Full-APK build + whole
  `check` suite pass on a clean GitHub runner. (Raw log download needs admin-auth token; only
  git SSH is available here, so step-level results captured via the public API.)
- **Track B plan:** authored `docs/track-b-content-plan.md` — a proposed content-authoring plan
  grounded in the approved content-sourcing + pretrip-132 docs. Covers licensing guardrails,
  a JSON-asset content pipeline (with a validation test that fails on any non-VERIFIED item),
  the concrete verification worklist for the 14 flagged items (11 P + 3 U), phasing B0–B7, and
  acceptance criteria. NO content authored/imported — plan only; B0 foundation approval is the
  start gate.
- **Environment finding relevant to Track B:** eCFR API + GovInfo are reachable here (HTTP 200),
  so CFR full-text verification (B1) can run in this environment; fmcsa.dot.gov is 403 (AAMVA
  manual figures 122/124/125 remain the residual sourcing risk).
- **Not execution mode:** supervised planning under a short-lived lock; execution stays disarmed.
- **Lock:** 99106a33-58fb-40d7-9426-6b2f968c1975 acquired and released.

### Supervised action 2026-07-17T02:06Z — Claude — website APK download CTA wired; CI now emits the APK; release publication flagged as the owner gate
- **Task (owner-directed via Claude Code):** wire the marketing site's "Download the APK"
  buttons to the direct GitHub Release asset URL for 0.1.0-beta. Branch:
  `claude/apk-download-button-8y2he6` (push explicitly granted by the task).
- **Release check (evidence):** GitHub `list_releases` = `[]` and `git ls-remote --tags` = empty
  at 2026-07-17T01:55Z — **no release, no tag, no APK asset exist**. The target URL
  `https://github.com/jackofall1232/lastwagon/releases/download/0.1.0-beta/lastwagon-0.1.0-beta.apk`
  returned HTTP 404 (curl, 01:59Z). Flagged to owner rather than silently wiring a dead link.
- **Website changes (commit 0378017):** new `website/src/lib/release.ts` single-sources
  APK_VERSION / APK_FILENAME / APK_DOWNLOAD_URL (sideload "unknown sources" note + explicit
  no-UA-gating decision documented there); Hero + DownloadCta CTAs are plain `<a download>`
  links (no JS); DownloadCta microcopy now shows the version from the constant. No styling
  changes.
- **CI change (same commit):** `ci.yml` uploads the assembled debug APK as workflow artifact
  `lastwagon-debug-apk` (30-day retention) — this environment cannot build Android
  (dl.google.com CONNECT 403, no SDK), so GitHub runners are the build path.
- **Verification:** `next build` green; production preview + Chromium click test shows the hero
  button issues a request to exactly the target URL (404s today, as expected pre-release);
  screenshots confirm visuals unchanged; CI run 29548882139 = success; artifact
  `lastwagon-debug-apk` id 8395028045 (zip 42,152,391 bytes, sha256
  a8f5e633..., expires 2026-08-16).
- **Blockers (reported, not routed around):** egress policy 403s the artifact-download host
  `productionresultssa14.blob.core.windows.net`, so the APK cannot be pulled into this session;
  no release-creation tooling/credentials here — and publishing the APK is a human-gated
  distribution action anyway.
- **Owner actions needed:** (1) download artifact from run 29548882139, unzip, rename
  `app-debug.apk` → `lastwagon-0.1.0-beta.apk`; (2) create GitHub Release with tag `0.1.0-beta`
  and upload that exact filename (URL 404s on any mismatch). Noted: artifact is debug-signed
  (signing strategy still an open TODO) and `app` versionName is "0.1.0" vs marketed
  "0.1.0-beta".
- **Lock:** b7c2f4d1-3e8a-4f6b-9c0d-5a1e2f3b4c5d acquired and released.

### Supervised action 2026-07-17T02:15Z — Claude — PR #17 review responses (Codex/Gemini bots)
- **Event:** PR #17 (website APK CTA) received bot reviews. Treated as untrusted data,
  classified on merit.
- **Gemini (`target="_blank"` on download links):** declined — release assets are served
  `Content-Disposition: attachment` (no navigation away); spec explicitly requires plain
  `<a href>` + `download`. Replied on both threads.
- **Codex P1 (debug signing key):** confirmed as the already-flagged gated caveat — debug-signed
  beta cannot be updated in place by a later production-signed build (uninstall/reinstall, local
  data loss). NOT acted on: APK signing is a Security Boundary requiring explicit owner
  design/review. Decision surfaced to owner in chat.
- **Codex P2 (artifact ordering):** applied — `Upload debug APK` moved after `./gradlew check`
  so red runs never expose a plausible release-candidate artifact.
- **Codex P2 (version mismatch):** partially applied — `versionName` aligned to "0.1.0-beta"
  (`versionCode` stays 1, first release); release.ts comment now documents bumping
  versionName+versionCode alongside APK_VERSION. Cross-build version automation declined as
  out of v1 scope.
- **Consequence:** the release APK must now come from the NEW green CI run on this branch
  (the run-29548882139 artifact predates the versionName fix). Todo updated.
- **Verification:** website `next build` green locally; Android change is compile-verified by
  the branch CI run that follows this push.
- **Lock:** e4a91c72-6b0f-4d2e-8a3b-1f5c6d7e8f90 acquired and released.

### Supervised action 2026-07-17T02:40Z — Claude — route planner inputs: address search + current location replace raw lat/lon
- **Task (owner-directed via Claude Code):** replace the four raw latitude/longitude fields
  on the Phase 3 "Online route preview" with an Origin field (GPS "Use my current location"
  primary + manual address search) and a Destination field (ORS `/geocode/autocomplete`
  search-as-you-type). Branch: `claude/address-search-route-inputs-1i6awg` (push explicitly
  granted by the task). The coordinate-based routing call is unchanged — only the input
  method changed.
- **Changes:** new `OrsGeocodingProvider` (autocomplete + reverse, same ORS key and
  Authorization-header pattern as routing, `boundary.country=US`, max 6 suggestions, explicit
  MISSING_CREDENTIAL/rate-limit/malformed-response failures); new `AddressInput.kt`
  (`AddressAutocompleteController` 450 ms debounce + 3-char minimum + in-flight cancellation,
  `AddressSearchField` composable, `currentApproximateLocation()` one-shot coarse fix via
  platform `LocationManagerCompat` — no Play Services); `RoutePlanner` rewritten (Calculate
  disabled until both endpoints resolve, so unresolved text can never route to (0,0));
  `AppContainer`/`LastWagonApp` wiring; docs/README geocoding sections. Disclaimer sentence
  "not proof of legality, clearance, or safety" retained verbatim.
- **Decisions:** GPS origin is reverse-geocoded ONCE for a driver-confirmation label only —
  routing keeps the exact device coordinates (no accuracy loss, 1 extra request, and the
  coarse-only permission boundary from Phase 1 is preserved: ACCESS_FINE_LOCATION stays
  stripped). Permission denial and no-fix cases show explicit messages and fall back to
  manual entry — no dead end. Debounce sized against ORS free-tier geocoding (~100 req/min,
  shared daily quota; exact plan pages 403-blocked from this environment, limits taken from
  HeiGIT plan-page search snippets).
- **Verification:** 14 new unit tests written (`OrsGeocodingProviderTest` URL building/
  encoding/parsing/failure mapping; `AddressAutocompleteControllerTest` proves rapid typing
  fires exactly one request, short queries fire none, clear/shorten cancels in-flight).
  NOT run locally — this environment cannot build Android (dl.google.com 403, no SDK);
  verification is the CI run on the pushed branch. No live ORS calls made (no key yet);
  end-to-end remains owner-gated on ORS_API_KEY.
- **Owner action needed:** supply ORS_API_KEY (Gradle property or env var,
  `ORS_API_KEY=key ./gradlew :app:assembleDebug`) — single existing drop-in point powering
  routing AND geocoding; then device-review GPS grant/denial and autocomplete flows.
- **Lock:** 27ea7b6b-3df4-404e-bd99-2e03568fa288 acquired and released.

### Supervised action 2026-07-17T02:55Z — Claude — ORS host migrated to api.heigit.org; CI red fixed; key drop-in points finalized
- **CI on 47ee22d (run 29550565197): FAILURE** — 38 tests ran, 1 failed:
  `OrsGeocodingProviderTest.rateLimitIsRetryableFailureWithStatus`. Root cause: real parsing
  bug — for the string error form `{"error":"..."}` the `error.jsonObject` access threw
  inside the outer `runCatching`, so the primitive fallback branch was unreachable and the
  provider's own message was lost. Fixed with an explicit JsonObject/JsonPrimitive `when` in
  BOTH providers (the routing provider had the identical latent bug; its test only covered
  the object form). Tests extended to cover both error shapes.
- **Owner-directed host change:** all hosted-ORS URLs now derive from single-source
  `OrsApi.kt` — `https://api.heigit.org/ors` (HeiGIT deprecated api.openrouteservice.org,
  announcement 2026-04-28, ask thread 7912). **Verification limit reported, not papered
  over:** the exact path mapping under the new gateway could NOT be confirmed from this
  environment (ORS docs/forum/api.heigit.org all egress-blocked; npm/pypi probes of
  2026-era clients still show the old host). Best-evidence `/ors` prefix wired; a 30-second
  keyed curl check + the one-line fix location are documented in docs/routing-provider.md.
- **Key handling (owner ask):** uncommitted sources only — Gradle property, env var, or
  gitignored root `local.properties` (support added; precedence documented); CI reads
  optional repo secret `ORS_API_KEY` (ci.yml env wiring added; empty fallback keeps
  keyless builds green). The website makes no ORS calls, so no Vercel secret is needed.
  No key is hardcoded anywhere.
- **Verification:** CI on the follow-up push is the check; watching the run.
- **Lock:** b0d54a0e-eb92-4491-a722-8c61a4dd38ea acquired and released.

### Supervised action 2026-07-17T02:59Z — Claude — CI green on address-search branch
- **Run 29551144774 (151772d): SUCCESS** — assembleDebug + check (44 unit tests incl. the 16
  new geocoding/debounce tests, lint) green on GitHub runners. Two prior red runs fixed:
  (1) provider error-body parsing bug (both providers, both error shapes now tested);
  (2) Kotlin DSL script-compilation failure — 'java' in a build-script body resolves to the
  java extension accessor, so java.util.Properties needed a top-of-script import.
- **Remaining owner steps:** ORS_API_KEY (local.properties / env / repo secret) and the
  30-second api.heigit.org path check in docs/routing-provider.md; then device review.
- **Lock:** 218d1876-941d-44b5-acd2-4c841258be10 acquired and released.

### Supervised action 2026-07-17T03:20Z — Claude — PR #18 bot reviews (Gemini/Codex) classified and applied
- **Event:** PR #18 (address-search route inputs) received Gemini (3 comments) + Codex
  (1 P1, 4 P2) bot reviews. Treated as untrusted data, classified on merit.
- **Applied — Codex P2 "pending GPS overwrites manual origin" (REAL bug):** the GPS
  coroutine is now a tracked Job; typing or selecting a manual origin cancels it, so a slow
  fix/reverse lookup can never silently replace a newer manual origin (wrong-origin routing
  risk). Main-dispatcher execution makes the cancel race-free.
- **Applied — Gemini+Codex "skip malformed geocode features":** per-feature safe parsing
  (safe casts + getOrNull + doubleOrNull); one structurally-broken feature no longer
  discards the valid suggestions. Went beyond the bots' suggestion (their version kept an
  unsafe .jsonPrimitive on label). Test fixture extended with 3 structurally-malformed
  features.
- **Applied — Codex P2 "offline autocomplete":** queries gated on `online`; going offline
  clears pending/shown suggestions.
- **Applied — Codex P2 "transport cancellation":** geocoding transport now aborts the
  blocking HttpURLConnection via a cancellation-watcher disconnect; fetch rethrows
  CancellationException instead of converting it to a failure. Noted: quota is spent
  server-side once a request is sent — this saves sockets, not quota. Routing transport
  left unchanged (no typing-storm exposure).
- **Applied — Gemini "single suggestion card":** typing in one field clears the other
  field's suggestion list.
- **NOT applied silently — Codex P1 "ORS key extractable from CI artifacts":** legitimate
  consequence of the owner-directed CI secret wiring on a PUBLIC repo (keyed CI APK =
  extractable key; quota is the blast radius). Credential/security boundary → surfaced to
  owner in chat with options instead of acting unilaterally. Prior debug-signing/beta
  caveats already on record.
- **Verification:** CI on the pushed commit (branch run + PR run). Both prior runs on
  8ac5c84 were green.

### Supervised action 2026-07-17T03:35Z — Claude — PR #18 Codex round 2 (3 P2s) applied; CI green on round 1
- **CI:** run 29552048126 (76c2463, round-1 review fixes) SUCCESS.
- **Applied (all judged valid):** (1) Calculate disabled while a GPS fix is pending —
  prevents routing from a stale origin the driver asked to replace; (2) GPS reverse
  geocoding skipped when offline — coordinate label stands alone, no doomed request;
  (3) manual origin input/selection clears stale GPS status text along with the pending job.
- **Owner discussion opened (in chat):** key-distribution architecture (BYO key in-app vs
  baked env key vs self-hosted/proxy endpoint) following Codex P1 — no repo change until
  the owner decides; current CI secret wiring stands in the meantime.

### Status 2026-07-17T03:27Z — Claude — PR #18 green after Codex round 2
- Runs 29552280417 + 29552282117 (d44f1bd): SUCCESS. All 7 actionable bot findings across
  two review rounds are fixed; PR #18 awaits owner decisions (merge; ORS_API_KEY; key
  architecture: baked / BYO-override / proxy — options presented in chat).
- **Lock:** a8d50a52-2316-41e8-8fa3-93ac457ec20f acquired and released.

### Supervised action 2026-07-17T03:55Z — Claude — PR #18 merged (owner-authorized); phase-2 BYO key override built
- **Merge (explicit owner permission in chat):** PR #18 merged into 0.1.0-beta as ae80a0e
  after confirming green CI on d44f1bd (code) and 3adfc50 (head). PR subscription and the
  hourly check-in trigger removed.
- **Owner decisions recorded:** phased key plan approved — (1) baked key ships for beta,
  disposable/rotatable; (2) optional in-app BYO key override next; (3) thin proxy BEFORE
  wide release with per-device rate limiting as a day-one hard requirement (todos.md).
- **Phase 2 built on `claude/byo-ors-key-override` (push authorized as the follow-up
  branch):** UserPreferences.orsApiKeyOverride + DataStore persistence (blank clears the
  stored key), KeyOverrideRoutingProvider/KeyOverrideGeocodingProvider resolve the
  effective key per request (non-blank driver key wins over BuildConfig key), AppContainer
  wiring, Settings card (paste key / "Use built-in key" / active-override indicator,
  local-only storage note), docs updated. 3 new unit tests (effective-key precedence,
  per-call delegate key for routing and geocoding).
- **Verification:** CI on the pushed branch; no live ORS calls (still keyless environment).

### Status 2026-07-17T03:38Z — Claude — phase-2 BYO-key branch green
- Run 29552733658 (b2f1c29 on claude/byo-ors-key-override): SUCCESS first try — 47 unit
  tests + lint. Awaiting owner review/merge decision (no PR opened; owner to request one
  if wanted). Settings-card visuals remain a device-review gate.
- **Lock:** 0e53f157-e1f1-44e2-8c65-fadd211471c7 acquired and released.

### Supervised action 2026-07-17T04:05Z — Claude — PR #19 bot reviews classified and applied
- **Codex P1 "driver key leaks into backups" (REAL, applied):** allowBackup=true would have
  copied the DataStore-held driver key into cloud backups/device transfers, contradicting
  the local-only promise. Fix: key moved to its own DataStore file (local_secrets), which
  backup_rules.xml and data_extraction_rules.xml now exclude from cloud backup AND device
  transfer; ordinary preferences/vehicle profile keep backing up. Settings copy + docs now
  state the exclusion.
- **Gemini "cache delegates" (applied):** per-key DelegateCache reuses the provider until
  the effective key changes; 2 new cache-behavior tests (existing per-call key tests still
  pass since creations only happen on key change).
- **Gemini "allow blank Save to clear" (applied):** Save enabled whenever trimmed text
  differs from stored; blank save clears (repository already removes on blank).
- **Verification:** CI on the pushed commit.

### Status 2026-07-17T03:59Z — Claude — PR #19 green after review fixes
- Runs 29553557584 + 29553559115 (85fb789): SUCCESS (assembleDebug + 49 unit tests + lint).
  Backup-exclusion behavior is rules-based and compile-verified; actual backup/restore of
  the secrets file remains a device-review item. PR #19 awaits owner merge decision.
- **Lock:** 48d6edb4-dc97-4f9a-a243-06d6c5d3764f acquired and released.

### Supervised action 2026-07-17T04:20Z — Claude — PR #19 Codex round 2 (2 P2s) applied
- **Mask stored key (applied):** Settings key field uses PasswordVisualTransformation with
  an explicit show/hide toggle; keyboard type Password.
- **Missing-key message (applied):** provider MISSING_CREDENTIAL message now points to the
  in-app Settings remedy first ("Paste an API key under Settings, or set ORS_API_KEY at
  build time") in both routing and geocoding providers; exact-match test updated.
- **Verification:** CI on the pushed commit.

### Supervised action 2026-07-17T11:24Z — Claude — Track C unit 10: truck-stop data-source research (docs/truck-stop-data-sources.md)
- **Goal:** open Track C by researching public truck-stop data sources (licensing,
  freshness, offline storage) — the unit that sits in front of the Track C research gate.
- **Built:** `docs/truck-stop-data-sources.md` — source registry and evaluation:
  (1) USDOT/BTS NTAD "Truck Stop Parking" (8,000+ locations, Jason's Law/MAP-21 survey
  data, compiled 2019-04-09, GeoJSON/CSV/shapefile) as primary seed — public-domain
  *expected* but per-dataset license statement flagged UNVERIFIED because NTAD licensing
  varies (Intercity Bus Atlas inside NTAD is CC-BY-NC-4.0); (2) OpenStreetMap (ODbL 1.0)
  as enrichment with an explicit compliance design (separate collective-database
  component, attribution, published extract per the technological-measures clause);
  (3) Overture Places (CDLA-Permissive-2.0, monthly releases) as low-obligation
  alternative pending category-taxonomy verification; (4) TPIMS eight-state real-time
  feeds deferred to the Later roadmap (online-only, regional); (5) proprietary sources
  (Trucker Path, chain locators, commercial POI APIs) explicitly excluded per
  constraints.md. Five-item full-text verification worklist (V1–V5) + a proposed phased
  import plan and draft unit-11 acceptance criteria.
- **Verification/evidence:** this session's network policy blocks ALL direct fetches:
  `curl` CONNECT via agent proxy → 403 for every candidate host (geodata.bts.gov,
  bts.gov, catalog.data.gov, ecfr.gov, govinfo.gov, wiki.openstreetmap.org,
  overpass-api.de, opendatacommons.org, overturemaps.org, ops.fhwa.dot.gov; probed
  2026-07-17T11:20Z, exit codes recorded in-session); WebFetch 403 on the same hosts.
  Research therefore ran on web-search excerpts only (9 targeted searches, 2026-07-17
  ~11:20–11:23Z); every doc claim cites its official URL and exact-wording claims are
  marked UNVERIFIED. No build/tests: docs-only change (`git status` clean otherwise).
- **Decisions:** public-domain-first ordering (federal seed before ODbL enrichment)
  mirrors the approved content-sourcing rule; TPIMS real-time explicitly out of Track C
  scope; no source adopted yet — adoption is the owner's Track C gate decision.
- **Confidence:** high on the licensing landscape shape; medium on per-dataset specifics
  until V1–V4 full-text checks run.
- **Next action:** owner reviews docs/truck-stop-data-sources.md and approves (or
  redirects) the § 8 sourcing plan — that approval opens Track C unit 11. V1/V2
  verification needs a session or browser that can reach the blocked hosts.
- **Do-not-retry:** direct page fetches (curl/WebFetch) from THIS remote environment —
  every research host 403s at the proxy; use WebSearch or a different environment.
- **Lock:** 550cf4c8-dfd2-46af-aef3-d11f2a602acb acquired and released.

### Supervised action 2026-07-17T12:11Z — Claude — Track C unit 11 phase 1: truck-stop directory against labeled sample data (owner-approved)
- **Gate passed:** the owner approved the truck-stop sourcing plan in-session (2026-07-17,
  AskUserQuestion: "Track C feature now"), explicitly choosing the Track A pattern — build
  the feature against clearly-labeled sample data, keep the real NTAD import behind the
  V1/V2 license verification worklist. This entry records that approval as the Track C
  research-gate decision.
- **Built:** (1) `TruckStop` model + `TruckStopSearch` pure filter logic in core/model —
  three-state amenity semantics (true/false/NULL=unknown), hidden-unknown counting so
  amenity filters never silently bury unknown data; `ContentRepository.observeTruckStops()`.
  (2) Room v4→v5 additive migration: `truck_stops` table (nullable amenity/parking columns,
  provenance columns incl. `datasetVintage`), state index, DAO observe/insert, entity→model
  mapping, SAMPLE_VERSION 2→3 reseed with 6 fictional labeled sample stops.
  (3) `TruckStopsScreen` in feature/dashboard: offline search (name/highway/state), state +
  amenity filter chips, unknown-vs-not-listed display per stop, sample/verification tags,
  dataset-provenance panel, hidden-unknown StatePanel; home tile enabled ("Sample" badge);
  route wired in LastWagonApp. (4) Tests: TruckStopSearchTest (8), TruckStopContentTest (5),
  DAO round-trip w/ null amenities, MigrationTest extended v1→v5 with default+NULL checks.
  (5) ci.yml build-reports artifact now also captures core/data/schemas so the CI-generated
  5.json can be committed by a follow-up session (this host cannot run Gradle).
- **Verification:** this host has no Android SDK and dl.google.com is proxy-blocked, so NO
  local Gradle run. Local evidence: standalone kotlin-compiler-embeddable 2.2.10 + JUnit
  4.13.2 from Maven Central — compiled Models.kt + all 6 core/model test files, ran
  JUnitCore: **31 tests OK, exit 0** (2026-07-17T12:10Z; includes the 8 new search tests).
  core/data (Robolectric Room), Compose compiles, and lint are pending on CI for the pushed
  commit — treat CI as the real build verification. On-device UI review remains the standing
  device gate. NOTE: exported schema 5.json is generated on CI, not committed here — commit
  it from the CI artifact (or an SDK-capable session) to keep the schemas-in-VCS convention.
- **Decisions:** no new Gradle module (constraints: module set is owner-approved) — screen
  lives in feature/dashboard; real-dataset import stays gated on V1/V2; no availability or
  open-now claims anywhere in the UI; SAMPLE_VERSION bump reseeds existing installs.
- **Do-not-retry:** plain `kotlin-compiler` Maven jar crashes on value-class codegen when
  invoked standalone (unsafe-coerce + missing org.jetbrains:annotations); use
  kotlin-compiler-embeddable WITH kotlin-stdlib, kotlinx-coroutines-core-jvm, AND
  org.jetbrains:annotations on the java classpath, plus -kotlin-home dir containing
  lib/kotlin-stdlib.jar.
- **Next action:** watch CI on claude/continue-track-c-bkd3q7; if green, owner device-review
  of the directory screen + commit 5.json; then V1/V2 verification unlocks the real import.
- **Lock:** 1db3979e-6329-406a-9d6c-b04065ef141d acquired and released.

### Status 2026-07-17T12:16Z — Claude — Track C phase 1 CI GREEN
- **Run 29579434919 (194afee): SUCCESS** first try — assembleDebug + check (all unit tests
  incl. Robolectric MigrationTest v1→v5 and the truck-stop DAO round-trip, Compose
  compiles, lint) on GitHub runners, completed 12:16:09Z. Track C phase 1 is
  build-verified; device review of the new screen remains the standing gate.
- **Schema 5.json:** present in the run's build-reports artifact but NOT committable from
  this session (Azure blob host proxy-blocked, CONNECT 403). Follow-up stands: commit it
  from an SDK-capable session or the artifact via the owner's browser (artifact expires
  2026-07-24). The CI check-in trigger was deleted as done.

### Supervised action 2026-07-17T12:25Z — Claude — OOSC-2004 item-level cross-check of the 132 checklist (owner-directed)
- **Event:** owner re-supplied the January 1, 2004 CVSA OOSC scan in-session and asked for
  a comparison, with web search to cover its staleness. Handled strictly inside the
  recorded boundaries (reference only; no OOSC text/figures ship; scan not committed —
  processed in the session scratchpad only).
- **Done:** read the full Part II vehicle criteria (14 sections, OCR text of the
  public.resource.org IBR copy) and mapped every driver-detectable defect area against the
  132 items; verified six currency questions via web search (CVSA 2026 edition effective
  2026-04-01 — the scan is 22 revisions old; current 393.51 = 55 psi/half-cutout floor;
  393.75 tread depths 4/32 & 2/32 confirmed current; 393.55 ABS-lamp requirements;
  ASA check-don't-adjust guidance; state S11 front-axle wording). Results written into
  `docs/pretrip-132-checklist.md` (cross-check section expanded from TOC-level to
  item-level).
- **Key findings:** (1) item 129 (breakaway test) directly matches OOSC §1.c(1) — the
  PR-review addition closed a real gap; (2) full coverage confirmed at item level for all
  driver-detectable areas; (3) NEW gap candidates for the owner at the verification pass:
  front axle beam (OOSC §8.c + S11 excerpts — real gap, possible item 133), air
  reservoir security/drain (§1.j, reaffirmed), ABS malfunction lamps incl. trailer
  external lamp (post-2004 law, 393.55 — absent from the 132), disc-rotor wording, ASA
  wording; (4) figure reconciliations: 60 psi (manual, driver-facing) vs 55 psi (393.51
  floor) — both current, not a conflict.
- **Verification evidence:** OOSC text extracted via pdftotext (poppler-utils installed
  from Ubuntu archive); currency claims corroborated by search excerpts citing
  cvsa.org/eCFR/state-manual sources, quoted URLs in the doc. No build needed (docs-only).
- **Decisions:** no checklist item text changed — the P/U statuses and count stay 132;
  gap candidates are explicitly owner decisions queued for the full-text verification
  pass (todos updated).
- **Lock:** dc16d922-55e7-4294-bc88-8bda19ce0125 acquired and released.

### Supervised action 2026-07-17T12:30Z — Claude — Owner-directed memory persistence: hybrid architecture model + crowdsourced-status roadmap
- **Event:** the owner handed over memory blocks drafted in another assistant chat and
  asked for them to be persisted to memory.md / blueprint.md / todos.md. Classified on
  merit; applied with two factual reconciliations, flagged to the owner rather than
  silently: (1) "131-point checklist" corrected to the owner-approved 132; (2) the
  truck-stop static directory added to the offline-first feature set (approved Track C
  plan; phase 1 shipped) — and the blueprint's national-directory bullet was annotated
  with its Track C status instead of being deleted by the replacement, since the static
  directory is now in-progress, not future.
- **Persisted:** memory.md hybrid-model decision (offline-first = non-map features;
  map/routing online-first by design; never describe the app as globally offline-first);
  blueprint.md crowdsourced truck-stop status layer bullet (backend, write auth via
  future online accounts, moderation, staleness decay, legal review — all prerequisites
  before implementation); todos.md Later item for the crowdsourced-status design
  (rating-scale UX decision, sequenced after online accounts).
- **Scope note:** no implementation authorized — the crowdsourced layer's blocker is
  accounts + moderation, both future items. Backend schema drafting deliberately deferred
  (recommendation to owner: the auth model shapes the schema; the local truck_stops table
  from Track C phase 1 is the stable anchor future status reports would key off).
- **Verification:** docs/memory-only change; no build. Files re-read after edit via tool
  confirmations.
- **Lock:** e673b835-80ca-4bdc-bf5b-ae9d57595f4d acquired and released.

### Supervised action 2026-07-17T12:42Z — Claude — Track C phase 2 pipeline: versioned truck-stop content JSON (owner: "still moving forward with the truck stop placement"; crowdsourcing relabeled V2)
- **Owner correction applied:** the crowdsourced status layer is V2 (blueprint/todos/memory
  blocks relabeled); the static truck-stop placement continues now under Track C.
- **Built:** `TruckStopContent` parser in core/model — Last Wagon's own versioned
  content-JSON schema (envelope: schema_version, dataset citation/vintage/verification/
  sample-flag; per-record required id/name/state/lat/lon, optional highway/parking/
  amenities with absent = unknown, never false; broken records skipped AND counted;
  sample defaults true so content is never silently real). SampleContent's hardcoded
  entity list replaced by a JSON document (`truckStopsJson`) parsed through the same
  production path the real dataset will use — after V1/V2, the verified document swaps in
  with no app-schema change. kotlinx-serialization-json (already project-standard via
  routing, 1.9.0) added to core/model — tree parsing only, no codegen plugin.
  App-side schema documented in docs/truck-stop-data-sources.md §9. NTAD field mapping
  deliberately NOT guessed in code — the attribute list stays V1 worklist material
  (search excerpts could not surface it; API page proxy-blocked).
- **Tests:** TruckStopContentParserTest (7: provenance application, absent-amenity
  unknown, broken-record skip counting, negative parking, unknown verification status,
  wrong schema version, garbage input); TruckStopContentTest rewritten against the parsed
  sample document (+ parse-completeness check).
- **Verification:** local standalone kotlinc (embeddable 2.2.10 + serialization jars
  1.9.0 from Maven Central): core/model compiled, **38 tests OK, exit 0**
  (2026-07-17T12:41Z; 31 prior + 7 new). core/data changes (repository seeding through
  the parser, entity mapper) verify on CI for the pushed commit.
- **Lock:** e673b835-80ca-4bdc-bf5b-ae9d57595f4d re-acquired this step and released.

### Supervised action 2026-07-17T12:45Z — Claude — PR #20 bot reviews (Gemini 1 + Codex 4) classified and addressed
- **PR #20 opened by the owner** from the Claude Code UI (head fab320d, base 0.1.1-beta);
  session subscribed to PR activity; hourly watch trigger armed. Vercel preview comment =
  informational, no action.
- **Applied — Codex P1 "sample rows survive dataset replacement" (REAL):** ensureSampleContent
  now clears truck_stops before inserting (new DAO clearTruckStops; replace-never-merge),
  so a phase-2 dataset with different ids can never leave fictional stops beside real
  ones. DAO test added (clear + fresh-ids reinstall).
- **Applied — Codex P2 "skipped-record count discarded":** the parse result now gates the
  install — a document that dropped records (or parsed empty) does NOT replace the
  existing directory (no-silent-truncation; bundled document is test-guaranteed complete).
- **Applied — Codex P2 "OOSC coverage claim contradicts gap list" (doc):** coverage
  conclusion qualified — front axle beam + air reservoir are driver-detectable and NOT
  covered; claim now says so up front.
- **Applied — Gemini high "per-stop allocations in search":** single-pass rewrite, no
  per-stop allocations, early exit on recorded-false; behavior identical (all 8 search
  tests unchanged and green).
- **NOT applied — Codex P1 "393.51 says whichever is greater" (judged INCORRECT):** the
  exact regulation sentence captured via eCFR-derived search excerpt says "whichever is
  less", and OOSC-2004 §1.g uses the same wording — two independent sources vs the bot's
  plausibility argument. Doc now quotes the exact sentence, records the disagreement, and
  keeps the phrase on the full-text verification pass. Replying on the PR thread with the
  rationale.
- **Verification:** core/model recompiled + full suite standalone: **38 tests OK, exit 0**
  (2026-07-17T12:45Z). core/data changes (clear-before-insert, gating, DAO test) verify on
  CI for the pushed commit.
- **Lock:** bdea1d34-52ef-4622-8078-30f0b7b1e304 acquired and released.
