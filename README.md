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
- offline operation with no account, analytics, or Internet permission

Truck-stop discovery and commercial-truck routing/GPS remain future research items and
have no implementation in this foundation.

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

Core operation is local and offline. The app requests no Internet permission and contains
no accounts, cloud sync, ads, purchases, analytics, maps, or location access. Resetting
progress removes local completion records while leaving bundled sample content installed.

## Content policy

Production inspection and CDL material must be researched from FMCSA resources, official
CDL manuals, applicable regulations, and other approved lawful sources. Proprietary
commercial question banks or training text must not be copied. Every production content
bundle requires provenance, licensing review, and versioning before inclusion.

## l00prite

This repository uses the l00prite protocol. Read `AGENTS.md` and `.l00prite/` before
working, and update ledger/state/TODO memory before stopping.
