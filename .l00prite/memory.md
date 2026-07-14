# Project Memory

Durable project facts and decisions that future agents should preserve.

## Decisions

- trkrhlpr is Android-only and will initially be distributed as a sideloaded APK; Google
  Play support is not required for the first production milestone.
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
- Android foundation architecture uses seven modules: app, core model/data/design/testing,
  and dashboard/learning features.
- Minimum Android version is API 26; compile/target SDK is 36; Java target is 17.
- Manual constructor injection is sufficient for the current dependency graph.
- Room stores versioned content/progress; DataStore stores theme/accessibility preferences.
- SDK-36-compatible pins include Core 1.17.0 and Lifecycle 2.10.0.

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
- The app manifest requests no Internet, location, account, analytics, or mapping permission.

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
