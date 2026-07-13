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
- This initialization is protocol-only; it does not authorize Android scaffolding or implementation.

## Facts

- Mission: "trkrhlpr exists to help commercial drivers perform safer inspections, become
  better educated, and reduce preventable violations through practical, offline-first tools."
- The initial audience is new and working commercial truck drivers in the United States.
- The product is intended as a practical daily companion, not merely exam preparation.
- Commercial-truck routing is safety-critical and must never be treated as ordinary
  passenger-car navigation.
- Exact inspection content, CDL questions, official sources, and unresolved product choices
  require research and explicit decisions before implementation.

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
