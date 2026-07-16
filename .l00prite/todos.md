# Prioritized TODOs

## Next
- [ ] Complete Phase 1 physical phone/tablet review: rendering, lifecycle, controls,
  approximate-location grant/denial, network loss, and accessibility.
- [ ] Routing Phase 3: implement replaceable online HGV routing with ORS as the initial
  provider and full request/response provenance.
- [ ] Routing Phase 4: require route overview and driver review before navigation.
- [ ] Routing Phase 5: implement licensed corridor pre-fetching, local route persistence,
  explicit offline/stale state, and safe off-corridor behavior (routing MVP boundary).
- [ ] Research authoritative FMCSA and official CDL-manual sources for the 131-point checklist.
- [ ] Define the exact checklist scope, sequence, vehicle configurations, and defect criteria.
- [ ] Define objective acceptance criteria for Study Mode and Real Inspection Mode.
- [ ] Research lawful authoritative sources and provenance rules for each CDL category.
- [ ] Define daily safety-question sourcing, review, and update rules.
- [ ] Run visual, accessibility, and connected Compose tests on representative phone and tablet hardware.
- [ ] Review and approve the production foundation before importing authoritative content.
- [ ] Implement CDL mock exams feature with randomized tests and readiness scoring.
- [ ] Define test-history, missed-question review, randomized-test, and readiness-scoring behavior.
- [ ] Implement CDL mock exams feature based on the defined behavior.
- [ ] Implement comprehensive truck stop locations dataset and search features.

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
- [x] 2026-07-16: Completed Routing Phase 0 safety contract and terminology approval.
- [x] 2026-07-16: Implemented Routing Phase 1 read-only MapLibre evaluation map, replaceable
  style provider, explicit attribution/limitations, zoom controls, and opt-in approximate
  location with denial handling; device review remains an exit check.
- [x] 2026-07-16: Implemented Routing Phase 2 schema-versioned vehicle profile with
  US-customary input, canonical metric persistence, plausibility validation, explicit
  confirmation/reconfirmation, hazmat state, axle data, and supported avoidances.
- [x] 2026-07-16: Approved and documented the phased commercial-truck routing plan,
  qualified safety language, mandatory driver review, and Phase 5 MVP boundary.
- [x] 2026-07-13: Confirmed product direction, first milestone, roadmap exclusions, technology, and mission.
- [x] 2026-07-13: Initialized the canonical l00prite protocol without application scaffolding.
- [x] 2026-07-14: Selected API 26 minimum, SDK 36, Kotlin/Compose module architecture.
- [x] 2026-07-14: Added Room/DataStore persistence and versioned representative content.
- [x] 2026-07-14: Added design system, adaptive navigation, and representative screens.
- [x] 2026-07-14: Built the APK, passed unit tests/lint, and compiled Compose UI tests.
