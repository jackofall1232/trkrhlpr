# Constraints

Hard rules, user preferences, security boundaries, and architecture constraints.

## Hard Rules
- Scaffolding generates files only; it does not execute implementation.
- Existing files must not be silently overwritten.
- Every implementation loop must update `.l00prite/` memory before stopping.
- Do not invent inspection procedures, defects, CDL questions, answers, regulations, or citations.
- Do not copy proprietary commercial study materials or question banks.
- Do not scaffold or implement the Android application during protocol initialization.

## User Preferences
- Android only; initial distribution is a sideloaded APK.
- Native Kotlin, Jetpack Compose, Android SDK 36 (Android 16).
- Offline-first and useful for daily driver work, not only exam preparation.
- Leave unresolved decisions as TODOs rather than fabricating answers.

## Security Boundaries
- Do not add credentials, analytics, remote services, accounts, or cloud synchronization
  without explicit approval and documented privacy/security requirements.
- APK signing, distribution, update, backup, and user-data handling require explicit design
  and review before release.
- Safety-critical guidance must show appropriate limitations and must not claim to replace
  official rules, required training, employer procedures, or driver judgment.

## Architecture Constraints
- Target native Kotlin and Jetpack Compose using Android SDK 36 (Android 16).
- Preserve offline-first operation for the first production milestone.
- Google Play support is not required for the MVP.
- Truck-stop discovery, truck GPS/routing, online accounts, and cloud synchronization are
  excluded from the first production milestone.
- Do not choose unconfirmed module boundaries, storage technology, dependencies, minimum
  Android version, or detailed application architecture without human approval.

## Content, Legal, and Licensing Boundaries

- Research FMCSA materials, official state CDL manuals, applicable regulations, and other
  official sources before implementing inspection or educational content.
- Record provenance and verification for regulated and safety-critical content.
- Confirm licenses and rights for use, attribution, modification, redistribution, and
  offline storage before adopting external content, location data, or map data.
- Do not assume access to proprietary truck-stop, study, or routing datasets.
- Commercial-truck routing requires dedicated safety, validation, warning, update, and
  legal review before any future implementation.

## Autonomous-Edit Denylist

Machine-readable glob list of paths an Execution Mode run must **never** auto-edit. A file
about to be edited that matches any glob below is treated as the
`destructive_operation_required` run boundary: the loop stops and asks for explicit per-action
human permission. This block is **protocol-adjacent and loop-immutable** — a run may never
remove or loosen an entry to get past a stop (doing so is itself the `human_review_gate`
boundary). Edit it yourself, before you arm a run. `scripts/l00prite-doctor.js` warns if this
block is missing.

```gitignore
# Secrets & credentials
.env
.env.*
**/secrets/**
**/credentials/**
**/*_key*
**/*_secret*
# Auth, money, and data safety
auth/**
payments/**
billing/**
**/migrations/**
# Infrastructure & deploy
.terraform/**
k8s/production/**
# Protocol files (never agent-edited during a loop)
.l00prite/prompts/**
.l00prite/LOCKING.md
```

### Auto-merge allowlist (default: none)

Nothing is auto-merged by default — push/merge/deploy always need per-action human permission.
If you ever allow auto-merge for trivial changes, list the exact safe paths here (e.g. docs or
comment-only edits). Behavior changes, dependency bumps, lockfile edits, and any denylisted
path are never eligible.
