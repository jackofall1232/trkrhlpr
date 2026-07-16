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
