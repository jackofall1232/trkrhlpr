# Project Blueprint

## Mission
Last Wagon (final product name; repository `trkrhlpr`) exists to help commercial drivers perform safer inspections, become better
educated, and reduce preventable violations through practical, offline-first tools. The
initial audience is new and working commercial truck drivers in the United States. The
product is intended to become a daily companion for safer inspections and continuing
education, not merely a way to pass the CDL exam.

## Confirmed Product and Technical Direction

- Android only; Google Play support is not required for the first production milestone.
- Initial distribution is a sideloaded APK.
- Native Kotlin, Jetpack Compose, and Android SDK 36 (Android 16).
- Offline-first operation and local progress tracking.
- No application architecture, modules, storage engine, source tree, or build configuration
  was selected during protocol initialization.

## Implemented Foundation Architecture

- Seven modules separate application composition, models/contracts, offline data,
  design-system components, testing support, dashboard/settings, and learning flows.
- Compose UI consumes repository Flows; Room persists versioned content and progress;
  DataStore persists user preferences.
- Manual constructor injection is centralized in the application container.
- Phone navigation uses a bottom bar; larger widths use a navigation rail.
- Representative content is fictional, local, visibly labeled, and versioned.

## First Production Milestone

### 131-point pre-trip inspection

Provide a structured 131-point commercial-vehicle checklist. Exact items, sequence,
vehicle applicability, inspection guidance, and defect criteria require authoritative
research and verification before implementation.

**Study Mode** must eventually let a driver browse each item, learn what to inspect and
why, understand common defects or failure conditions, and learn the correct sequence.

**Real Inspection Mode** must eventually guide a driver through an actual pre-trip,
support marking each item complete, show clear completion progress, and reduce accidental
omissions. Timestamps, notes, and optional defect reporting are planned capabilities whose
precise milestone acceptance criteria remain TODOs.

### CDL practice tests

Provide lawful, authoritative study material and practice tests for Class A, Class B,
General Knowledge, Air Brakes, Combination Vehicles, Hazmat, Tanker, and Passenger.
Planned capabilities include multiple-choice questions, explanations for correct and
incorrect answers, category and randomized tests, progress tracking, missed-question
review, test history, and readiness scoring. Exact MVP acceptance criteria remain TODOs.

### Daily safety question

Provide one short practical fleet-safety question per day, with answer choices, a correct
answer, a short explanation, and optionally an official source or regulation reference.
Completion or streak tracking is planned. Topics may include defensive driving,
hours-of-service awareness, following distance, weather, railroad crossings, cargo
security, fatigue, distraction, inspections, and emergency procedures.

### Local and offline behavior

The first production milestone includes local progress tracking and offline-first
operation. Exact persistence, backup, export, reset, migration, privacy, and update
behavior remain TODOs.

## Future Roadmap — Excluded from the First Production Milestone

- National directory of truck-accessible stops, fuel, parking, rest areas, weigh stations,
  services, repairs, washes, showers, restaurants, overnight parking, and amenities.
- Open-source commercial-truck GPS and routing based on vehicle dimensions, weights,
  axles, hazmat restrictions, prohibited roads, clearances, bridges, tunnels, and other
  commercial-routing constraints.
- Online accounts.
- Cloud synchronization.
- Possible CDL categories such as School Bus and Doubles/Triples.

These are roadmap items only. The truck-stop directory and truck routing are major future
subsystems and must not be represented as part of the initial milestone.

## Authoritative Research and Safety Gates

- Research current FMCSA materials, official state CDL manuals, applicable regulations,
  and other official sources before writing inspection procedures or CDL content.
- Determine and record the authoritative basis and provenance for every inspection item,
  defect explanation, test question, answer, and regulatory reference.
- Do not copy proprietary commercial study materials or question banks. Practical safety
  providers may inspire educational style, but their proprietary text and questions must
  not be copied.
- Determine licensing, attribution, redistribution, modification, and offline-storage
  rights before adopting any external content or dataset.
- Do not assume access to proprietary truck-stop or map data. Later research must evaluate
  open data, public APIs, community contributions, verification, and refresh strategies.
- Truck routing is safety-critical and is not passenger-car navigation. Before any later
  implementation, research map and restriction coverage, routing engines, offline maps,
  updates, validation, legal exposure, warning language, and safe failure behavior.
- Truck routes are decision support based on the supplied vehicle profile and available
  data. Never claim that a route is "truck-safe," "guaranteed clear," or "STAA-compliant."
  Missing restrictions are unknown rather than evidence of clearance. Require driver
  review against signs, official restrictions, permits, dispatch instructions, and current
  conditions, and obtain legal review before release.
- The application must not be presented as a replacement for official regulations,
  required training, employer procedures, driver judgment, or legally required inspections.

## Requirements

- [ ] Research and approve the authoritative 131-point checklist and inspection sequence.
- [ ] Define and approve objective Study Mode acceptance criteria.
- [ ] Define and approve objective Real Inspection Mode acceptance criteria.
- [ ] Define and approve lawful CDL content sources and testing acceptance criteria.
- [ ] Define and approve daily safety-question content and provenance rules.
- [ ] Define local progress tracking and offline-first behavior.
- [ ] Define minimum supported Android version and Android SDK compatibility strategy.
- [ ] Define accessibility, privacy, data retention, backup, export, reset, and update requirements.
- [ ] Keep all unresolved product and technical choices as TODOs until a human confirms them.

## Definition of Done

- [ ] Detailed first-milestone requirements and acceptance criteria are approved.
- [ ] Safety-critical and regulated content is traceable to reviewed authoritative sources.
- [ ] Content and data licensing obligations are documented and satisfied.
- [ ] All approved first-milestone features work offline on supported Android devices.
- [ ] Local progress behavior is tested, including failure and migration scenarios once defined.
- [ ] Required safety and limitation warnings are reviewed.
- [ ] Verification evidence is recorded for every implemented requirement.
- [ ] Truck-stop, truck-routing, account, and cloud-sync systems remain outside the milestone.
- [ ] A human approves production readiness and the sideloaded APK release process.

## Non-Execution Boundary
This blueprint is guidance for later implementation loops. Scaffolding tools must not execute the project unless a human explicitly starts an implementation session.
