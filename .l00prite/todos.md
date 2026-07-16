# Prioritized TODOs

## Next-week build-out plan (Execution Mode, owner-requested 2026-07-16)

Scope: **first milestone + truck-stop directory**. Routing Phases 6-8 excluded. Execution
Mode is NOT armed — next week starts with the `execute-loop.md` pre-flight display and an
explicit in-session EXECUTE confirmation. CI (`.github/workflows/ci.yml`) now gives each
iteration real build verification on GitHub runners. Sequence the loop units so gated work
waits behind its gate:

- **Track A — buildable now, against labeled sample content (no content-approval gate):**
  1. Content-schema migration: per-item source citation, verification status, applicability
     flags (`COMBO`/`AIR`/`IF-EQUIPPED`). (Room migration — reviewed.)
  2. Study Mode: browse the checklist by section; show inspect-for, defects, sequence.
  3. Real Inspection Mode: mark items complete, visible progress, reduce skipped items,
     filter by the driver's vehicle configuration via the flags.
  4. Mock-exam engine: randomized tests, scoring, missed-question review, test history,
     readiness scoring (define behavior first, then implement).
  5. Daily safety question mechanism: one-per-day selection, answer/explanation, streak.
  6. Local progress: acceptance criteria + persistence/migration tests.
- **Track B — behind human gates (docs approval + full-text verification):**
  7. Author the 132 checklist items as real content (original prose, per-item provenance).
  8. Author CDL class + endorsement practice questions and daily questions (original prose).
  9. Import real content, replacing labeled sample content (needs foundation approval).
- **Track C — behind its own research gate:**
  10. Research public truck-stop data sources (licensing, freshness, offline storage).
  11. Truck-stop dataset + search feature.

Gates that will pause the loop: content-doc approval (Track B start), full-text verification
of the 13 flagged checklist items (needs network to eCFR/FMCSA), foundation approval before
import, and the truck-stop data-source research gate (Track C start).

## Next
- [ ] Complete Phase 1 physical phone/tablet review: rendering, lifecycle, controls,
  approximate-location grant/denial, network loss, and accessibility.
- [ ] Complete Phase 3 keyed ORS integration/device review: valid route, no-route, timeout,
  quota/authorization error, corrupt response, map fit/overlay, and route deletion.
- [ ] Complete Phase 4 device/accessibility review: long overview scrolling, warning
  visibility, acknowledgment persistence, bypass attempts, stale state, and profile changes.
- [ ] Verify the Last Wagon rename and Phase 5 with the full Gradle suite (unit tests,
  debug APK, instrumentation-test APK, lint) in an environment with Google Maven access;
  the 2026-07-16 remote session could not reach dl.google.com.
- [ ] Complete Phase 5 device/exit review: corridor download progress and size, airplane
  mode viewing, expiry and stale marking, cancellation/deletion, storage behavior, and
  off-corridor warning with approximate location.
- [ ] Obtain written confirmation of OpenFreeMap public-instance offline-prefetch terms
  (or move to self-hosting/a keyed commercial provider) before any production release.
- [x] 2026-07-16: Owner approved `docs/content-sourcing.md` and
  `docs/pretrip-132-checklist.md` (content basis, licensing boundaries, and the product-
  enumeration framing). Docs human-review gate passed; real content authoring may proceed
  after foundation approval + full-text verification.
- [ ] Resolve the 11 PARTIAL and 3 UNVERIFIED checklist items and the flagged UNVERIFIED
  facts in `docs/content-sourcing.md` by line-by-line verification against the FMCSA model
  manual PDF and eCFR full text — needs a session/machine whose network can fetch them
  (this remote environment's policy blocks those hosts).
- [ ] Define objective acceptance criteria for Study Mode and Real Inspection Mode.
- [ ] Design the content-schema additions (per-item source citation, verification
  date/status, applicability flags COMBO/AIR/IF-EQUIPPED) as a reviewed Room migration
  before authoritative content import.
- [ ] Run visual, accessibility, and connected Compose tests on representative phone and tablet hardware.
- [ ] Review and approve the production foundation before importing authoritative content.
- [ ] Implement CDL mock exams feature with randomized tests and readiness scoring.
- [ ] Define test-history, missed-question review, randomized-test, and readiness-scoring behavior.
- [ ] Implement CDL mock exams feature based on the defined behavior.
- [ ] Implement comprehensive truck stop locations dataset and search features.
- [ ] Review the marketing website v1 (`website/`), finalize CTA copy and the signed-APK
  download link, and explicitly approve any Vercel deployment before it happens.

## Later
- [ ] Routing Phase 6: implement guided navigation and tested degraded-state handling.
- [ ] Routing Phase 7: research, validate, and integrate restriction and clearance assurance
  without treating missing data as proof of clearance.
- [ ] Routing Phase 8: complete controlled driver beta and privacy, security, licensing,
  accessibility, legal, and human safety reviews.
- [ ] Define accessibility, privacy, local-data retention, backup, export, reset, and migration requirements.
- [ ] Define APK signing, sideload distribution, and update strategy.
- [ ] Define inspection timestamps, notes, and optional defect-reporting behavior.
- [ ] Consider School Bus, Doubles/Triples, and other CDL categories after the initial scope.
- [ ] Revisit online accounts and cloud synchronization only after explicit future approval.

## Done
- Move completed items here with dates when helpful.
- [x] 2026-07-16: Researched authoritative FMCSA/CDL-manual sources for the 131-point
  checklist and defined its exact scope, sequence, vehicle applicability, and per-item
  check criteria with citations and verification statuses (now
  `docs/pretrip-132-checklist.md`, renamed from -131- when the count moved to 132).
  Key finding: a fixed point count is not an official term — it is Last Wagon's enumeration
  of Section 11 + 49 CFR content and must be presented as such.
- [x] 2026-07-16: Researched lawful, publicly available sources and provenance rules for
  every first-milestone CDL category and the daily safety question
  (`docs/content-sourcing.md`). Key finding: the AAMVA model CDL manual (even on
  fmcsa.dot.gov) is AAMVA-copyrighted, SDLA-only — reference-only; CFR text and
  FMCSA-authored materials are public domain; AAMVA secure test items and CVSA OOSC are
  excluded.
- [x] 2026-07-16: Defined daily safety-question sourcing, review, and update rules
  (public-domain-first citation rule, currency re-verification, safety framing) in
  `docs/content-sourcing.md`.
- [x] 2026-07-16: Built the marketing website v1 scaffold under `website/` (Next.js +
  Tailwind, locked palette/type system, ported hero rig animation, features,
  how-it-works mockups, download CTA), verified via production build and Chromium
  screenshots including prefers-reduced-motion.
- [x] 2026-07-16: Renamed the product to its final name "Last Wagon" everywhere (label,
  applicationId com.lastwagon.app, namespaces, classes, docs); repository stays trkrhlpr.
- [x] 2026-07-16: Implemented Routing Phase 5 offline route corridor: bounded MapLibre
  geometry-region download along the reviewed route with detail levels, tile estimation,
  6,000-tile cap, progress, cancellation, deletion, and 7-day expiry; per-provider
  prefetch permission with OpenFreeMap Liberty for development (production confirmation
  gated); explicit OFFLINE/STALE states; no offline rerouting; coarse-location
  off-corridor stop-and-reassess warning. Full Gradle build verification pending.
- [x] 2026-07-16: Completed Routing Phase 0 safety contract and terminology approval.
- [x] 2026-07-16: Implemented Routing Phase 1 read-only MapLibre evaluation map, replaceable
  style provider, explicit attribution/limitations, zoom controls, and opt-in approximate
  location with denial handling; device review remains an exit check.
- [x] 2026-07-16: Implemented Routing Phase 2 schema-versioned vehicle profile with
  US-customary input, canonical metric persistence, plausibility validation, explicit
  confirmation/reconfirmation, hazmat state, axle data, and supported avoidances.
- [x] 2026-07-16: Implemented Routing Phase 3 replaceable ORS HGV provider, explicit failure
  states, exact redacted request/response provenance, atomic saved-route storage, unreviewed
  coordinate-based route preview, warning handling, and MapLibre geometry overlay.
- [x] 2026-07-16: Implemented Routing Phase 4 mandatory overview and driver-review gate,
  exact route/profile-bound acknowledgment persistence, visible route states, legacy-route
  migration as unverified, and fail-closed map access.
- [x] 2026-07-16: Approved and documented the phased commercial-truck routing plan,
  qualified safety language, mandatory driver review, and Phase 5 MVP boundary.
- [x] 2026-07-13: Confirmed product direction, first milestone, roadmap exclusions, technology, and mission.
- [x] 2026-07-13: Initialized the canonical l00prite protocol without application scaffolding.
- [x] 2026-07-14: Selected API 26 minimum, SDK 36, Kotlin/Compose module architecture.
- [x] 2026-07-14: Added Room/DataStore persistence and versioned representative content.
- [x] 2026-07-14: Added design system, adaptive navigation, and representative screens.
- [x] 2026-07-14: Built the APK, passed unit tests/lint, and compiled Compose UI tests.
