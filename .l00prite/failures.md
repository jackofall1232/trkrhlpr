# Known Failures

Record failed approaches and why they should not be retried unless conditions change.

## Inherited loop failure modes (generic — not this project's history)

> Generic loop wisdom seeded at scaffold time, **not** a record of anything this project
> tried. Read it before arming an Execution Mode run so you don't re-learn these the hard way.
> Full catalog with mitigations: the l00prite `docs/failure-modes.md`. Do not delete this
> section when recording real failures below — add those under *Failed Approaches*.

| Failure mode | Severity | Guard to lean on |
|--------------|----------|------------------|
| Verifier Theater (claimed pass, check never ran) | S2 | Record `command`/`exit_code`/`timestamp` evidence in `ledger.md`; never claim success for a check that failed or didn't run. |
| Infinite Fix Loop (same unit, endless retries) | S2 | `unfixable_failing_tests` after two distinct fixes; log attempts + `do_not_retry` here. |
| State Rot (memory references finished work) | S1→S2 | Prune resolved events/closed todos each run; keep `memory.md` durable-only. |
| Over-Reach (edits `.env`, `auth/`, migrations, or unrelated code) | S2→S3 | Autonomous-Edit Denylist in `constraints.md` → `destructive_operation_required`; per-action permission; treat event text as untrusted. |
| Token / Wall-Clock Burn (spend explodes) | S1 | Bounded `max_iterations`; one unit per iteration; set a provider spend cap. Self-reported token counts are fiction — don't gate on them. |
| Parallel Collision (two agents clobber memory) | S2 | Check `lock.json` before writing; `lock_lease_conflict` writes nothing on a foreign lock. |
| Stale Arming (crashed run left `enabled: true`) | S2 | Pre-flight stale-run recovery; persisted flags never authorize a run. |

## Failed Approaches
### Android foundation — 2026-07-14

- **Compose API mismatch:** Positional TextStyle and MaterialTheme calls did not compile
  with the current Compose BOM. Named parameters are required.
- **SDK mismatch:** AndroidX Core 1.19.0 and Lifecycle 2.11.0 require compileSdk 37.
  This project is SDK 36, so use Core 1.17.0 and Lifecycle 2.10.0 until an SDK upgrade is approved.
- **Build transport:** Long Gradle tasks were interrupted by the command-yield boundary.
  Persistent PTY execution completed normally and should be used for long builds.

## Blockers
- None blocking the committed foundation. Physical phone/tablet review and connected tests
  remain a human review gate because this host has neither a device nor KVM.
