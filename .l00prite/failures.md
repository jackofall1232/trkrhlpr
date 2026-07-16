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

### Remote-session network policy — 2026-07-16

- **Google Maven blocked:** The Claude Code remote environment's proxy allowlist blocked
  `dl.google.com` (and maven.google.com redirects there), so AGP/androidx/Android SDK
  components could not be fetched and full Gradle Android builds were impossible there.
  Maven Central and services.gradle.org were reachable. Workaround: standalone
  kotlin-compiler from Maven Central to compile and JUnit-test pure Kotlin logic, plus
  compile checks against the MapLibre classes.jar and Robolectric android-all API 36.
- **Masked pipeline failure:** `./gradlew ... | tail` reported exit 0 because the pipeline
  status came from `tail` while the wrapper download had failed. Check `PIPESTATUS` or run
  without pipes before recording build evidence.

### Build-environment setup (this Claude Code host) — 2026-07-16 (Execution run)

- **Masked exit via trailing echo:** `./gradlew … > log 2>&1; echo "EXIT $?"` run in the
  background reports the *echo's* success as the task exit code (0) even when Gradle BUILD
  FAILED. The background-task completion notification's "exit code 0" is therefore NOT proof of
  a green build. Always grep the log for `BUILD SUCCESSFUL|BUILD FAILED` and the captured
  `GRADLE_EXIT=` line before recording verification. (Same family as the masked-pipeline note
  above.)
- **`SDK location not found` for Android-library unit tests:** unlike the earlier remote
  session, THIS host both reaches `dl.google.com` AND ships a complete SDK at
  `/opt/android-sdk` (cmdline-tools/latest, platforms/android-36, build-tools/36.0.0, licenses
  accepted). Gradle just needs pointing at it. Fix: gitignored `local.properties` with
  `sdk.dir=/opt/android-sdk` (and/or `export ANDROID_HOME=/opt/android-sdk` per invocation —
  env vars do NOT persist across this harness's Bash calls). This installs nothing; do not
  treat it as a dependency change. With it, `:core:data:testDebugUnitTest` builds green.
- **Note vs prior session:** the failures.md "Google Maven blocked / standalone
  kotlin-compiler workaround" note is env-specific to the older remote host and does NOT apply
  here — prefer real `./gradlew` verification on this host.
- **AGP 9.2.1 test-asset source set DSL:** the top-level form
  `sourceSets.getByName("test").assets.srcDir(...)` throws at configure time
  ("DefaultAndroidLibrarySourceSet_Decorated cannot be cast to AndroidLibrarySourceSet"). Use
  the block form `sourceSets { getByName("test") { assets.srcDir("$projectDir/schemas") } }`
  instead (needed to expose exported Room schemas to Robolectric MigrationTestHelper). `srcDir`
  warns as deprecated but works.
- **Robolectric + compileSdk 36:** pin the emulated SDK via
  `src/test/resources/robolectric.properties` (`sdk=33`) so Robolectric need not support
  API 36; Room migration SQL is SDK-independent. Robolectric 4.14.1 + androidx.room:room-testing
  + androidx.test:core 1.6.1 work for JVM migration/DAO tests here.
- **New-schema-version first-build race:** the build that FIRST generates schema `N.json`
  (KSP export) can fail its MigrationTestHelper test with `FileNotFound … /N.json` because the
  unit-test asset merge ran before the export. The exported schema is on disk afterward, so a
  simple re-run passes (and the schema should be committed to VCS). Not a schema-content
  mismatch — distinguish by the message (`Cannot find the schema file` vs an expected/found
  column diff).
