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
