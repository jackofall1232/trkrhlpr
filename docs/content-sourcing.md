# Authoritative Content Sourcing and Provenance

This document records the researched, publicly available sources for Last Wagon's regulated
and safety-critical content — the 132-point pre-trip inspection, CDL class and endorsement
study material and practice tests, and the daily safety question — together with the
licensing boundaries and authoring rules that every piece of imported content must satisfy.

Research method and limits: this registry was compiled 2026-07-16 in a remote session whose
network policy allowed web search with source excerpts but blocked full-document fetches.
Every source below was corroborated by excerpts from the cited official URL. Facts that
could not be corroborated are marked **UNVERIFIED** and must be checked against the full
source text before any content that depends on them ships. Nothing in this document
authorizes importing content into the app; the foundation-review human gate still applies.

## 1. Licensing landscape — what may and may not be used

| Source class | Status | Use in Last Wagon |
|---|---|---|
| CFR text (49 CFR via eCFR/govinfo) | US government work — public domain (17 U.S.C. § 105) | Quote, adapt, store offline. Cite part/section. |
| FMCSA web content and PDFs authored by FMCSA (CMV Driving Tips, Our Roads Our Safety, HOS summary, Cargo Securement Handbook) | US government work — public domain | Quote, adapt, store offline. Watch for contractor-produced or licensed imagery inside PDFs. |
| AAMVA Model CDL Manual (2005 CDL Test System, July 2010/2014 revisions) and state CDL manuals derived from it | **Copyright © AAMVA.** Reproduction granted to State Driver Licensing Agencies only, even though FMCSA hosts the PDF | Verification reference only. Never copy or closely paraphrase its prose. Do not commit the PDF to this repository. Facts and procedures it documents are usable when re-expressed originally and cross-cited to public-domain regulation where possible. |
| AAMVA secure knowledge-test item pool ("2005 Test Item Summary Forms", 49 CFR § 383.133) | Confidential, SDLA-only | Never sourced, reproduced, or approximated. All practice questions are original. |
| Commercial CDL prep providers (question banks, courses) | Proprietary | Never sourced or copied. Style inspiration at most, per the blueprint. |
| CVSA North American Standard Out-of-Service Criteria | Proprietary, paid, restricted PDF (~$50, updated annually) | Excluded as a text source. Public defect criteria come from Appendix A to 49 CFR Part 396 instead. CVSA International Roadcheck press materials (public) may inspire topics only. The owner supplied a January 1, 2004 edition scan for internal reference (2026-07-16): used only to cross-check category coverage, never as a source of shipped text or figures — it is CVSA-copyrighted, 20+ years superseded (annual revisions), and stays out of this repository. OOSC editions are incorporated by reference in federal rules (e.g., 49 CFR 385.415(b)(1)), which does not make the text freely redistributable. |
| PHMSA Emergency Response Guidebook (ERG 2024) | Free PDF and apps from PHMSA. **UNVERIFIED:** joint US/Canada/Mexico work — public-domain status of the whole book is not established | Reference and link. Verify front matter before reproducing content; reproduction questions go to ERGComments@dot.gov. |
| 17 U.S.C. § 105 caveat | Public-domain status applies in the US; protection abroad is not waived | Relevant only if the app distributes outside the US. |

Key finding: hosting on fmcsa.dot.gov does **not** make the model CDL manual a government
work. The specific state manuals checked (Delaware, Louisiana, Arkansas) carry "COPYRIGHT ©
2005 AAMVA" with SDLA-only reproduction rights. Section 383.131 requires each state's driver
information manual to be *comparable to* the AAMVA model, which is why their text is
near-identical in practice. "Comparable to" is a content requirement, not proof that any
given state's text is AAMVA-copyrighted — a state could in principle author independently
comparable text. So the rule is conservative and **per-manual**: treat every state CDL
manual as reference-only unless that specific manual's own copyright/reuse terms are checked
and found to permit the use. Do not assume a manual is public domain merely because a state
published it, and do not assume every manual is AAMVA-copyrighted by logic alone — verify
the actual notice on the copy in hand.

## 2. Authoring rules for all imported content

1. **Original prose only.** Study text, item guidance, defect examples, questions, answers,
   and explanations are written fresh for Last Wagon. No sentence is copied or closely
   paraphrased from an AAMVA-derived manual or any commercial product.
2. **Per-item provenance.** Every inspection item, question, and explanation records: the
   authoritative basis (CFR cite and/or manual section used for fact verification), the
   verification date, and a verification status. The current Room content schema has no
   provenance fields; adding them (plus vehicle-applicability flags) is a schema change that
   needs review before content import.
3. **Public-domain first.** Where a fact exists in both the CFR and the CDL manual, cite the
   CFR. Manual-only facts (e.g., test-day procedure details) cite the manual as *reference*,
   expressed in original words.
4. **No invented content.** An item, defect, figure, or rule that cannot be traced to a
   recorded source does not ship. UNVERIFIED marks block release, not just review.
5. **Currency checks.** Regulations change. Each content category records a "verified
   against source on <date>" stamp; re-verify at authoring time and before each release.
   Known moving targets: HOS split-sleeper pilot program (Federal Register 2025-09-17) and
   the § 392.10 rail-crossing rulemaking (Federal Register 2025-05-30).
6. **Safety framing.** Content is education support, never a substitute for official
   regulations, required training, employer procedures, or driver judgment (blueprint rule).
7. **Test-question boundaries.** Practice questions test knowledge the CFR requires
   (§§ 383.111–383.123) using facts verified against public sources. They must not
   replicate the style, structure, or specific scenarios of actual state exams, and the app
   must say plainly that its questions are practice items, not real exam questions.

## 3. Source registry — pre-trip inspection (132-point checklist)

Detailed item-level mapping lives in `docs/pretrip-132-checklist.md`.

| Source | What it provides | URL |
|---|---|---|
| 49 CFR § 392.7 (pre-trip satisfaction) | The parts a driver must be satisfied are in good working order before driving | https://www.ecfr.gov/current/title-49/subtitle-B/chapter-III/subchapter-B/part-392/subpart-B/section-392.7 |
| 49 CFR § 396.13 / § 396.11 | Pre-trip inspection duty; driver vehicle inspection reports | https://www.ecfr.gov/current/title-49/subtitle-B/chapter-III/subchapter-B/part-396 |
| 49 CFR Part 393 | Required parts and accessories (lights §§ 393.9–.33, brakes §§ 393.40–.55, fuel §§ 393.65–.69, coupling §§ 393.70–.71, tires § 393.75, emergency equipment § 393.95, frames/wheels/steering/suspension §§ 393.201–.209, cargo securement §§ 393.100–.136) | https://www.ecfr.gov/current/title-49/subtitle-B/chapter-III/subchapter-B/part-393 |
| Appendix A to 49 CFR Part 396 | Public defect/rejection criteria per system (brakes, coupling, exhaust, fuel, lighting, steering, suspension, tires, wheels, glazing, frame) | https://www.ecfr.gov/current/title-49/subtitle-B/chapter-III/subchapter-B/part-396/appendix-Appendix%20A%20to%20Part%20396 |
| CDL manual Section 11 (Pre-Trip Vehicle Inspection Test) | The inspection walk-around structure, item lists, and "check for" guidance every state tests against | Reference only (AAMVA copyright). State copies: https://dmv.ny.gov/brochure/cdl10sec11-13.pdf and equivalents |
| 49 CFR § 383.113(a)(1) | Federal skills requirement that drivers can inspect engine compartment, cab/start, steering, suspension, brakes, wheels, side, rear, coupling, plus air-brake checks | https://www.ecfr.gov/current/title-49/subtitle-B/chapter-III/subchapter-B/part-383/subpart-G/section-383.113 |

## 4. Source registry — CDL classes and knowledge tests

Federal basis (all public domain, eCFR Part 383):

- § 383.91 vehicle groups: Group A combination ≥ 26,001 lb GCWR where the **GVWR of the
  vehicle(s) being towed** exceeds 10,000 lb (the rating, not the current load); Group B a
  single vehicle ≥ 26,001 lb GVWR, **or such a vehicle towing a unit whose GVWR is not over
  10,000 lb** (so a heavy straight truck with a small trailer is still Group B, not Group A);
  Group C anything not in A or B that either transports 16+ people (including the driver) or
  carries **placardable hazardous material** (material requiring placards under 49 CFR part
  172 subpart F, per the § 383.5 definition — not any hazmat).
- § 383.110–.111 required knowledge: 20 general areas, plus air-brake areas and
  combination-vehicle areas. (**UNVERIFIED:** exact (a)(1)–(a)(20) paragraph numbering and
  the individual air-brake/combination sub-area texts — verify against full § 383.111.)
- § 383.113 required skills (pre-trip, basic control, on-road).
- § 383.131 driver manuals; § 383.133 test methods (FMCSA-approved question pool, retake
  needs a different version); § 383.135 passing standard — **at least 80% on every
  knowledge test**; failing the air-brake component adds the L restriction; failing the
  combination component bars a Group A license.
- § 383.95 restrictions: L (air brakes), Z (air-over-hydraulic).

Manual reference structure (AAMVA model; verification reference only): Section 1
Introduction, Section 2 Driving Safely, Section 3 Transporting Cargo Safely, Section 5 Air
Brakes, Section 6 Combination Vehicles, Sections 11–13 skills tests. Confirmed against NY,
CA, MI, PA, RI, OR, WI, MA state copies (URLs recorded in the research ledger entry).

Typical structure (Utah DLD, official; other states vary): General Knowledge 50 questions,
Air Brakes 25, Combination 20; all pass at 80% (the 80% floor is federal, § 383.135).
**UNVERIFIED:** per-state counts beyond Utah — present counts as typical, not universal.

## 5. Source registry — endorsements

| Endorsement | Federal knowledge basis (public domain) | Manual reference | Underlying regulations (public domain) |
|---|---|---|---|
| Hazmat (H) | 49 CFR § 383.121 — knowledge drawn from 49 CFR parts 171, 172, 173, 177, 178, 397 | Section 9 | HMR 49 CFR 171–180 (placard Table 1 any amount / Table 2 at 1,001 lb+, shipping papers, loading rules); Part 397 driving/parking/attendance/route rules; TSA security threat assessment 49 CFR Part 1572 (fee $85.25 as of 2025); ERG (PHMSA, free) |
| Tanker (N) | 49 CFR § 383.119 — surge cause/prevention, braking empty/full/partial, baffled vs unbaffled, tank types, product densities, grade/curvature effects | Section 8 | § 383.5 tank-vehicle definition: individual rated capacity > 119 gal and aggregate ≥ 1,000 gal, permanent or temporary. (**UNVERIFIED:** "high center of gravity" wording as a § 383.119 item — it is manual/test content; verify full CFR text) |
| Passenger (P) | 49 CFR § 383.117 — loading/unloading, emergency exits, emergencies, rail crossings and drawbridges, braking | Section 4 | § 392.62 bus operation (standee line per § 393.90); § 392.63 towing/pushing loaded buses; § 392.10 rail crossings (stop 15–50 ft, no shifting on tracks) |

Roadmap-only records: School Bus (S) § 383.123 / Section 10; Doubles/Triples (T) § 383.115
/ Section 7. Out of the first milestone by blueprint.

## 6. Source registry — daily safety question

All primary sources below are FMCSA-authored US government works (public domain):

- **CMV Driving Tips series** (8 crash-causation topics): buckle up, too fast for conditions
  (reduce speed ⅓ on wet roads, ½ or more on snow), unfamiliar roadway, inadequate
  surveillance/No-Zone, driver fatigue (drowsy windows 12–6 a.m., 2–4 p.m.), distraction,
  following too closely (under 40 mph: 1 s per 10 ft of length; over 40 mph: add 1 s),
  inadequate evasive action. https://www.fmcsa.dot.gov/safety/driver-safety/cmv-driving-tips-overview
- **Our Roads, Our Safety** tip sheets and visor cards: work zones, safe speed, hazardous
  weather, blind spots. https://www.fmcsa.dot.gov/ourroads
- **Hours of Service** (Part 395 + FMCSA summary): 11-hour driving / 14-hour window /
  30-minute break required after 8 cumulative hours of **driving time** (this is the current
  post-Sept-2020 § 395.3(a)(3)(ii) trigger — driving time, not on-duty time and not elapsed
  clock time) without at least one 30-minute interruption of driving status; the qualifying
  interruption may be off-duty, sleeper-berth, **or on-duty-not-driving** time. (A reviewer
  asked for the older "8 hours elapsed since last break" framing; that is the pre-2020 rule
  and was declined — the current codified text is driving-time-based.) / 60-70 hour limits /
  split sleeper berth: one period
  of at least 7 h in the sleeper plus a second of at least 2 h (off duty or sleeper),
  together totaling at least 10 h — commonly a 7/3 or 8/2 split.
  **UNVERIFIED details:** 14-hour non-pausing wording and the adverse-conditions +2 h
  extension were corroborated only from secondary excerpts; re-verify on the official page,
  and watch the 2025 split-sleeper pilot. https://www.fmcsa.dot.gov/regulations/hours-service/summary-hours-service-regulations
- **Driver's Handbook on Cargo Securement** (49 CFR 393 Subpart I, illustrated, free).
- **Part 392 driving rules**: § 392.3 ill/fatigued; § 392.10 rail crossings; § 392.14
  hazardous conditions (reduce speed, stop when sufficiently dangerous); § 392.80 texting
  ban; § 392.82 hand-held phone ban (penalty amounts live in Part 386 appendices —
  **UNVERIFIED**, cite carefully).
- CDL manual Section 2 topic list (fog, winter, hot weather, mountain driving, skids,
  fires, alcohol/drugs, staying alert…) — topic checklist reference only; daily-question
  text sources from the public-domain materials above.

Sourcing rule for daily questions: every question cites at least one public-domain source;
each is reviewed against the safety-framing rule before import; topics rotate across the
blueprint's list (defensive driving, HOS awareness, following distance, weather, rail
crossings, cargo security, fatigue, distraction, inspections, emergencies).

## 7. Verification gaps to close before content import

1. Full-text verification of every UNVERIFIED mark above against eCFR (blocked in this
   session's network policy; needs a normal-network session or local machine).
2. Line-by-line verification of the 132-point checklist against CDL manual Section 11 and
   Appendix A to Part 396 (see `docs/pretrip-132-checklist.md` for per-item status).
3. Decide the ERG's role after checking its front-matter reproduction terms.
4. Add provenance and applicability fields to the content schema (Room migration — needs
   review) before authoritative content replaces the labeled sample content.
5. Human review gate: foundation approval before importing any of this content (existing
   todo), and safety/legal review of user-facing limitation wording before release.
