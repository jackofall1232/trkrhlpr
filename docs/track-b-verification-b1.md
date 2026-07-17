# Track B — B1 Verification Pass (flagged items)

Status: **verification results, for human review — uncommitted.** Produced 2026-07-17 by an
agent fact-check against **eCFR full text** (49 CFR, current, via the eCFR *versioner XML API*
`https://www.ecfr.gov/api/versioner/v1/full/2026-07-01/title-49.xml`). The eCFR HTML section
pages 302-redirect and could not be fetched directly; `fmcsa.dot.gov` was 403 and not relied
on. **Every citation below should be spot-checked against the live eCFR URL before authored
prose ships** — this report is evidence to review, not a substitute for the source.

Bottom line: the CFR-anchored items verify cleanly; the three air-brake **figures** do not have
the app's stated numbers in the CFR and must be handled per the drop/PARTIAL policy.

## Confirmed — may ship as VERIFIED with the CFR citation

| Item / fact | Citation | Note |
|---|---|---|
| 13 — steering column condition | 49 CFR § 393.209(c)–(d) | CFR term is "steering **column**", not "shaft" — fix the item name |
| 33 — driver seat belt assembly required | 49 CFR § 393.93 | § 393.93 requires the *assembly*; the *use/wear* mandate is a **separate** § 392.16 — cite that if the app implies "must wear" |
| Defect criteria: steering, suspension, coupling (5th wheel/kingpin), wheels/rims, tires, brakes | Appendix A to 49 CFR Part 396 | These are **periodic-inspection defect criteria** — do NOT label them "out-of-service" (OOS = CVSA, copyright-restricted) |
| 129 — trailer air-supply / breakaway | 49 CFR § 393.43(a),(b),(d) | Tractor-protection auto-close in the 20–45 psi window; trailer breakaway brakes apply automatically and hold ≥ 15 min — codified |
| CDL knowledge test passing standard = "at least 80 percent" | 49 CFR § 383.135(a)(1) | verbatim |
| Vehicle groups A/B/C (towed-unit GVWR > 10,000 lb → Group A; hazmat/16-pax → Group C) | 49 CFR § 383.91(a) | verbatim; Group C says "used in the transportation of hazardous materials as defined in § 383.5" (our "placardable hazmat" is a paraphrase) |

## Partially confirmed — requirement is codified, but the in-cab test procedure / exact figure is not

| Item | What IS codified | What is NOT codified (manual/test convention → re-express originally, cite manual as reference, never as CFR) |
|---|---|---|
| 131 — trailer brake hold ("tug") test | brakes must hold — § 393.40 / § 393.41; breakaway hold — § 393.43(d) | the tug-test *procedure* itself |
| 132 — service-brake check | brake performance — § 393.52 (e.g. 43.5% braking force, 40 ft stop from 20 mph) | the "roll at ~5 mph and apply" *procedure* |
| 122 — air build-up | 85→100 psi build-up spec — FMVSS 121, 49 CFR § 571.121 S5.1.1 (a capacity-based **formula**, ≈25 s at max governed rpm) | the pre-trip **"within 45 seconds"** figure — a CDL-manual convention, NOT in the CFR |
| 124 — governor cut-in | **minimum** cut-in — § 571.121 S5.1.1.1 (truck ≥ 100 psi, bus ≥ 85 psi) | any fixed cut-in/cut-out **pair** or specific cut-out psi (manufacturer-set) |

## Unconfirmed — must NOT ship as fact (drop the figure, or keep the item as PARTIAL per the pipeline policy)

| Item | Finding |
|---|---|
| 125 — static air leakage "≤ 2 psi/min single / ≤ 3 psi/min combination" | **No such threshold is codified anywhere in 49 CFR** (checked § 393.45, § 393.52, § 571.121, Appendix A to Part 396 — the latter treats leaks only qualitatively, "any audible leak"). These numbers originate in the AAMVA CDL manual / CVSA (copyright-restricted). Per policy: keep item 125 as **PARTIAL** with a verificationNote, excluded from ship, until a public primary source is found or the figure is dropped with an explicit `expectedItemCount` change. |

## Wording fixes to apply during authoring (from the verification)

1. Item 13: "steering column", not "steering column/shaft".
2. Item 33: distinguish the assembly requirement (§ 393.93) from the wear mandate (§ 392.16).
3. Do not call Appendix A / Part 396 criteria "out-of-service" — they are periodic-inspection defect criteria.
4. Air figures 122/124/125: ship only the codified minimums; the "45 s", fixed cut-out, and 2/3-psi-per-minute numbers are manual conventions, not CFR.
5. HOS (daily-question content, § 395.3(a)(3)(ii)): the codified wording is "more than **8 hours of driving time**" — **drop the word "cumulative"** used in `content-sourcing.md`; qualifying interruption includes off-duty, sleeper-berth, **or on-duty-not-driving** time.

## Recommended status updates to `pretrip-132-checklist.md` (for owner approval)

- 13, 33, 129 → **VERIFIED** (CFR-anchored above).
- 66, 70, 81, 95, 99, 114, 131, 132 → remain **PARTIAL**: their underlying requirement often has a CFR/Appendix-A anchor, but the Section-11 walk-around *wording/procedure* is manual convention to be re-expressed originally at authoring time; verify each against Appendix A during B3.
- 122, 124 → **PARTIAL** (ship codified minimums only).
- 125 → **PARTIAL/UNVERIFIED** (no public figure) — excluded from ship.

## Still open from `content-sourcing.md` (verify during B3/B4 authoring)

§ 383.111 exact (a)(1)–(a)(20) numbering; § 383.119 tanker wording; § 383.117 passenger; ERG
front-matter reproduction terms; Part 386 penalty amounts. All to be checked against eCFR (API
route) before the dependent content ships.
