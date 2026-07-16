# trkrhlpr

An offline-first Android companion designed to help commercial drivers perform safer
inspections, keep learning, and reduce preventable violations.

> **Foundation status:** Current content is representative interface data only. It is not
> an authoritative pre-trip procedure, CDL question bank, regulation, or substitute for
> training, employer procedures, required inspections, or driver judgment.

## Foundation

The production foundation is native Kotlin and Jetpack Compose, targeting Android 16
(API 36) with a minimum of Android 8.0 (API 26). It includes:

- a dark-first industrial design system with adaptive phone/tablet navigation
- representative Study and Inspection Mode interactions
- representative CDL practice question, explanation, and result flow
- a local daily safety question interaction
- local progress and confirmed reset flow
- Room-backed versioned content/progress storage
- DataStore-backed theme and accessibility preferences
- offline core tools with no account or analytics
- an online, read-only MapLibre evaluation map with optional approximate location
- a locally stored, validated, driver-confirmed commercial-vehicle profile
- an unreviewed ORS HGV route preview using coordinate entry and local route provenance
- a mandatory route-overview and driver-review gate before route-map access

Truck-stop discovery and commercial-truck routing/GPS remain future research items and
remain outside the production milestone. Phase 3 can calculate and display an explicitly
unreviewed route preview; it is not navigation or a truck-safe-route guarantee.

## Project structure

| Module | Responsibility |
|---|---|
| `app` | Application, dependency container, adaptive navigation, Android entry points |
| `core:model` | Domain models and repository contracts |
| `core:data` | Room database, DataStore preferences, sample importer, repositories |
| `core:designsystem` | Theme tokens and reusable industrial Compose components |
| `core:testing` | Shared test fixtures and dependencies |
| `feature:dashboard` | Home, progress, settings, and about |
| `feature:learning` | Inspection, practice-test, and daily-question representative flows |
| `feature:routing` | Vehicle-profile workflow, read-only map, and replaceable style provider |

## Prerequisites

- JDK 17
- Android SDK Platform 36
- Android SDK Build Tools 36.0.0

Set `ANDROID_HOME`/`ANDROID_SDK_ROOT`, or create an untracked `local.properties`:

```properties
sdk.dir=/absolute/path/to/android-sdk
```

The checked-in Gradle wrapper downloads the documented Gradle version automatically.

## Build

```bash
./gradlew :app:assembleDebug
```

The installable debug APK is produced at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Test and lint

```bash
./gradlew testDebugUnitTest
./gradlew :app:assembleDebugAndroidTest
./gradlew lintDebug
./gradlew check
```

Run connected Compose tests when a compatible device or emulator is available:

```bash
./gradlew connectedDebugAndroidTest
```

Kotlin formatting conventions are defined in `.editorconfig`; Android lint fails on errors.

## Sideload the debug APK

Enable developer options and USB debugging on a test device, connect it, then run:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

The debug APK is for development and evaluation only. A production signing and secure
update process must be designed before distributing a release APK.

## Data and privacy

Core inspection and learning operation remains local and offline. The map preview uses the
Internet to load evaluation map data and requests approximate location only after the user
selects the location control; map viewing continues if permission is denied. The app has no
accounts, cloud sync, ads, purchases, or analytics. The confirmed vehicle profile is stored
locally in DataStore. Resetting progress removes local completion records while leaving the
vehicle profile and bundled sample content installed.

## Route provider configuration

Phase 3 is disabled when no OpenRouteService key is configured. Supply a development key
through an uncommitted Gradle property or environment variable:

```bash
ORS_API_KEY=your-development-key ./gradlew :app:assembleDebug
```

The key is compiled into the APK and can be extracted; do not distribute a build containing
a privileged or unrestricted key. Production credential handling requires a separate
security review. Route coordinates, geometry, request parameters, warnings, and provider
provenance are stored in the app's private files and can be deleted from the profile screen.
Review acknowledgments are bound to the exact route request and confirmed vehicle profile;
they are records of review, not certifications of legality, clearance, or safety.

## Content policy

Production inspection and CDL material must be researched from FMCSA resources, official
CDL manuals, applicable regulations, and other approved lawful sources. Proprietary
commercial question banks or training text must not be copied. Every production content
bundle requires provenance, licensing review, and versioning before inclusion.

## l00prite

This repository uses the l00prite protocol. Read `AGENTS.md` and `.l00prite/` before
working, and update ledger/state/TODO memory before stopping.
