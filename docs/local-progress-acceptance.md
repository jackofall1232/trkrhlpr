# Local Progress — Acceptance Criteria (proposed)

Status: **proposed, pending owner approval.** Objective acceptance criteria for the
first-milestone local-progress behavior. Scope is offline, on-device, single-user. No
accounts, no cloud sync (excluded from the milestone).

## Storage & durability
- All progress persists locally across app restarts and process death: inspection
  completions, practice attempts, and daily-question completions (Room); user preferences and
  the vehicle profile (DataStore).
- Schema changes ship as **additive, non-destructive Room migrations** with an explicit
  `Migration` and an exported schema JSON per version. No `fallbackToDestructiveMigration`.
  Current chain: **v1 → v2** (inspection-item provenance columns) → **v3** (day-keyed daily
  completions). Every migration is validated by `MigrationTest` (Robolectric) plus compile-time
  Room schema validation.

## Inspection progress
- An item's completion is a persisted boolean; toggling it is idempotent.
- "Required" progress = completed ÷ applicable items, where applicability is derived from the
  vehicle configuration (`APPLIES` items only). `NOT_APPLICABLE` items are excluded from the
  denominator; `IF_EQUIPPED` items are optional and never block completion.

## Daily safety question
- Exactly one question is selected per **UTC day**, deterministically from the pool.
- A day is recorded at most once (`daily_day_completions` keyed by `epochDay`).
- Streak = consecutive completed days ending today; it holds from yesterday until a day is
  actually missed, then resets. Verified by `DailySafetyTest`.

## Practice / tests
- Each answered practice question records an attempt (question, chosen answer, correctness,
  timestamp). Accuracy = correct ÷ attempts.

## Reset
- A single "reset all progress" clears inspection completions, practice attempts, and daily
  completions in one transaction, leaving content and preferences intact. Verified by
  `LastWagonDaoTest.resetProgressClearsDayCompletions`.

## Open / deferred (not in this unit)
- Timezone-aware "local day" for daily selection/streaks (currently UTC day index).
- Persisting the Inspection-Mode vehicle-configuration selection across launches.
- Backup, export, and per-item timestamps/notes.
- Connected (device) verification of executed migrations and UI progress rendering.
