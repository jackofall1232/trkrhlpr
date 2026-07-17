# Track B — Real Content Authoring Plan

Status: **plan (proposed).** Authoring/import execution is gated on **foundation approval**
(open todo). This plan defines *how* Track B runs once that gate is cleared; it authorizes
nothing by itself. Grounded in the two owner-approved docs — `docs/content-sourcing.md`
(licensing/provenance) and `docs/pretrip-132-checklist.md` (the enumerated checklist + per-item
verification status).

## 0. Objective

Replace the labeled **sample** content (inspection items, CDL practice questions, daily
questions) with **real, original-prose, source-verified** content for the first milestone —
populating the v2 provenance/applicability schema honestly, with no item shipping unverified.

## 1. Preconditions (gates)

| Precondition | State |
|---|---|
| `docs/content-sourcing.md` approved | ✅ owner-approved 2026-07-16 |
| `docs/pretrip-132-checklist.md` approved (count = 132) | ✅ owner-approved 2026-07-16 |
| Content schema carries provenance + applicability fields | ✅ shipped v2 (`sourceCitation`, `verificationStatus`, `applicabilityFlags`) |
| **Foundation approval to author/import real content** | ⛔ **OPEN — the Track B start gate** |
| Full-text verification of the 14 flagged items | ◑ now *possible here* (see §4) — must complete before import |
| Safety/legal review of user-facing limitation wording | ⛔ pre-release gate |

## 2. Non-negotiable licensing guardrails (from content-sourcing.md)

1. **Original prose only** — nothing copied or closely paraphrased from any AAMVA-derived
   manual or commercial prep product.
2. **Public-domain-first citation** — where a fact is in both the CFR and the CDL manual, cite
   the **CFR** (49 CFR via eCFR/govinfo, public domain). Manual-only facts cite the manual as
   *reference*, re-expressed originally.
3. **AAMVA model manual = reference only**, never copied; do **not** commit the manual PDF to
   this repo. AAMVA secure test items and CVSA OOSC text are **never** sourced.
4. **No invented content** — anything not traceable to a recorded source does not ship.
5. **Practice-question boundaries** — questions test CFR-required knowledge (§§ 383.111–.123),
   original scenarios only, never mimicking real state exams; the app labels them practice items.
6. **Safety framing** — education support, never a substitute for regulations, training,
   employer procedures, or driver judgment.

## 3. Content storage & import pipeline (recommended)

Move real content out of code into a **reviewable, versioned JSON asset** so content review is
diff-friendly and separate from logic:

- `core/data/src/main/assets/content/<version>/…json` — inspection categories + items, test
  categories + practice questions, daily questions. Each record carries the provenance fields:
  `sourceCitation`, `verificationStatus`, `verifiedOn`, `applicabilityFlags`, and
  `isSample=false`.
- A small, **unit-tested** parser (kotlinx.serialization is already a dependency) loads the
  asset and seeds Room, replacing `SampleContent` seeding. Bump a new `CONTENT_VERSION` so
  `ensureContent()` re-seeds on upgrade (mirrors the existing `SAMPLE_VERSION` mechanism).
- Keep the labeled sample content available for non-authoritative/demo builds if useful, but
  production builds seed only verified real content.
- A JSON **schema doc** + validation test: every item has a non-empty citation, a
  `verificationStatus` of `VERIFIED`, and valid flags; the build/test **fails** if any
  imported item is `PARTIAL`/`UNVERIFIED` (enforces "unverified blocks release" mechanically).

## 4. Verification pass — the 14 flagged items (do first, before authoring import)

Environment note: **eCFR API is reachable here** (`ecfr.gov`, `govinfo.gov` → 200), so CFR
full-text verification can run in this session. `fmcsa.dot.gov` is bot-blocked (403), which
matters only for the AAMVA-manual-hosted figures below (reference-only regardless).

**Group A — CFR-codified (verify now via eCFR, move to VERIFIED):**
- Item **13** steering column/shaft → 49 CFR **§ 393.209**.
- Item **33** safety belt → **§ 393.93** (and § 392.16 use).
- Defect/rejection wording for many P items → **Appendix A to 49 CFR Part 396** (steering,
  suspension, coupling, wheels, tires, brakes). Re-express originally, cite the appendix.

**Group B — Section-11 walk-around P items (66, 70, 81, 95, 99, 114, 129, 131, 132):**
procedure/wording that is manual/test content. Verify the *underlying requirement* in CFR
(Part 393 / Appendix A) where one exists, author original "inspect-for"/defect prose, and cite
the manual as reference only. Capture official Section-11 wording from a **reachable public
state copy** for cross-check accuracy only — never as shipped text.

**Group C — UNVERIFIED air-brake figures (122 build-up, 124 governor cut-in, 125 static
leakage):** these are manual/test-procedure numbers (psi/time), not codified in CFR. They need
the model manual (FMCSA-hosted, blocked here) or a reachable state copy to confirm the exact
figures, then original prose citing the manual as reference. **If a figure cannot be
confirmed from an accessible authoritative copy, it does not ship** — the item stays out of the
authoritative set rather than shipping a guessed number. This is the residual verification risk.

Each resolved item records: CFR/manual citation, `verifiedOn` date, and status → `VERIFIED`.
Also close the `content-sourcing.md` UNVERIFIED marks (§ 383.111 numbering, § 383.119 tanker
wording, HOS 14-h/adverse-conditions wording, penalty amounts, ERG front-matter terms).

## 5. Phasing

- **B0 — Foundation approval (owner gate).** Approve authoring/import of real content per this
  plan. *Nothing below runs until this clears.*
- **B1 — Verification pass.** Resolve all 14 flagged checklist items + the sourcing-doc
  UNVERIFIED marks against eCFR/manual; update `pretrip-132-checklist.md` statuses. Target:
  132/132 VERIFIED (any figure that can't be confirmed is dropped, with a note, not guessed).
- **B2 — Content pipeline.** Implement the JSON-asset content model + parser + validation test
  (§3), replacing sample seeding. No real content yet — infra + one migrated sample to prove it.
- **B3 — Author the 132 checklist** as original prose with per-item provenance/flags.
- **B4 — Author CDL practice questions** (Class A/B, General, Air Brakes, Combination, Hazmat,
  Tanker, Passenger) + explanations — original items, CFR-cited, 80% passing standard reflected.
- **B5 — Author daily safety questions** from public-domain FMCSA materials, topic-rotating.
- **B6 — Import** verified content (foundation approval already granted at B0 covers this),
  flip `isSample=false`, bump content version.
- **B7 — Safety/legal review** of user-facing limitation wording before any release.

Each of B1–B6 is a l00prite Execution unit with its own verification (parser/validator tests,
content-count and status assertions) and is committed on `0.1.0-beta`. B3–B5 are content-review
gated (human reads the authored prose before it lands).

## 6. Acceptance criteria (content Definition of Done)

- Every shipped inspection item, question, answer, and explanation is **original prose** with a
  recorded citation and `verificationStatus = VERIFIED` and a `verifiedOn` date.
- **No** `PARTIAL`/`UNVERIFIED` item ships (enforced by the validation test).
- Checklist item order, applicability flags, and the 132 enumeration match the approved doc;
  the app still states the count is Last Wagon's enumeration, not an official standard.
- CDL questions are labeled practice items (not exam questions); daily questions cite ≥1
  public-domain source; safety-framing/limitation wording is present and legal-reviewed.
- No AAMVA/CVSA/commercial text is copied; no manual PDF is committed.

## 7. Open decisions for the owner

1. **Approve B0 (foundation gate)** to start Track B under this plan. *(the one thing blocking start)*
2. Content storage: confirm the **JSON-asset** approach (§3) vs a Kotlin content object.
3. Scope of the first content drop: **checklist-only first** (recommended — highest value,
   most CFR-anchored), or checklist + CDL questions together.
4. Handling of any air-brake figure that can't be confirmed from an accessible authoritative
   copy: **drop it** (recommended) vs defer the whole air-brake section.
