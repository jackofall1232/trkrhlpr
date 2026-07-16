# 131-Point Pre-Trip Inspection — Researched Definition

This document defines Last Wagon's 131-point commercial-vehicle pre-trip inspection:
scope, sequence, every item, what to check, applicability, and the authoritative source
basis for each. It implements the blueprint requirement to research and verify the
checklist against authoritative sources before implementation. Licensing and authoring
rules are in `docs/content-sourcing.md`. This is a content definition for human review —
it does not authorize importing content into the app.

## What "131 points" means — required honesty

Research found **no official source that defines a "131-point" pre-trip inspection**. It is
not a term used by FMCSA, AAMVA, any state CDL manual, or any regulation. Official material
instead defines inspection *areas* and *items*: the CDL manual's Section 11 walk-around and
the parts lists of 49 CFR 392.7, 393, and 396. Item counts in official and training
material vary with enumeration granularity (roughly 60–150 for a tractor-trailer).

Therefore: **131 is Last Wagon's product enumeration** — a specific, fixed way of counting
the authoritative inspection content for a Class A tractor + semi-trailer with air brakes.
The app must present it as "Last Wagon's 131-point checklist, built from the CDL manual
inspection test and federal regulations," and must never call it an official standard,
an official count, or a guarantee of test coverage or legal compliance.

Point-count decision (owner, 2026-07-16): the owner offered to fall back to a 100- or
101-point checklist if 131 was not available. No fixed count — 131, 101, or 100 — exists in
any official source, so all are product enumerations; and the 131-item enumeration below is
fully mapped to authoritative content, so no fallback is needed. 131 stands (it also
matches the blueprint and existing UI copy). Re-cutting to another count would be a
branding choice, not an accuracy fix.

## Scope and vehicle applicability

- **131 is the full enumeration, not a per-vehicle count.** The 131 items are the complete
  set for a fully-equipped Class A tractor + single semi-trailer with air brakes. The number
  of items that actually *apply* to a given vehicle is always ≤ 131 and depends on its
  equipment — a leaf-spring tractor with no cargo lift, no sliding fifth wheel, and no
  washers has fewer applicable items than one with all of those. Real Inspection Mode shows
  the applicable count for the driver's configuration; the app must not imply every vehicle
  has 131 live points.
- **Primary configuration for authoring:** Class A tractor + single semi-trailer, air
  brakes — the most common configuration for the target audience, and the basis on which the
  enumeration was built.
- **Applicability flags:** items carry flags so other configurations shrink the list
  honestly instead of showing untestable items: `COMBO` (tractor-trailer only), `AIR`
  (air brakes), `IF-EQUIPPED` (air ride, sliding fifth wheel, cargo lift, washers, etc.).
  A straight truck (Class B) drops the coupling and trailer sections and the trailer brake
  checks; a hydraulic-brake vehicle swaps the air-brake checks for the hydraulic check
  (pump three times, apply firm pressure, hold five seconds, pedal must not depress —
  Section 11 brake-check procedure).
- **Test-version note:** since August 2022 two official skills-test versions coexist: the
  classic 2005 Section 11 inspection and the AAMVA "modernized" version (fewer,
  critical-safety-focused items; labeled 10M/11M in PA and GA materials). This checklist
  follows the **classic full walk-around**, which is a superset suited to real daily
  inspections; Study Mode should mention that state test versions differ.

## Sequence

Order follows the Section 11 flow used by state manuals, with brake tests last so the
vehicle is only moved after everything else passes: engine compartment (engine off) →
cab check and engine start → external light check → walk-around (steer axle done with the
engine compartment; side of tractor; drive axles; coupling; trailer) → brake checks.

## Sources cited in the tables

| Key | Source |
|---|---|
| S11 | CDL manual Section 11 (AAMVA model; verification reference only — corroborated across NY, MI, RI, NJ, GA, OR, CA, WA, PA state copies) |
| 392.7 | 49 CFR § 392.7 pre-trip parts list (verbatim-confirmed) |
| 393.x | 49 CFR Part 393 parts and accessories (Subpart B lamps §§ 393.9–.33; C brakes §§ 393.40–.55; F coupling §§ 393.70–.71; G tires § 393.75; H emergency equipment § 393.95; I cargo §§ 393.100+; J frames/wheels/steering/suspension §§ 393.201–.209) |
| 396A | Appendix A to 49 CFR Part 396, minimum periodic inspection standards (public defect criteria) |
| 396.13 | 49 CFR § 396.13 driver pre-trip duty; § 396.11 DVIR review |

Status column: **C** = corroborated from official-source excerpts; **P** = partially
corroborated (secondary sources echo the manual; official wording not yet captured);
**U** = unverified figure or wording — must be checked against the full source text before
this item ships.

## The 131 items

### 1. Engine compartment — engine off (items 1–17)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 1 | Ground under vehicle | No fresh oil, coolant, fuel, or grease puddles | S11 | C |
| 2 | Engine/transmission underside | No dripping fluids | S11 | C |
| 3 | Hoses | Good condition; no leaks | S11 | C |
| 4 | Engine oil level | Dipstick present; level above refill/add mark, within operating range | S11 | C |
| 5 | Coolant level | Reservoir sight glass level correct (or radiator level when cool) | S11 | C |
| 6 | Power steering fluid | Dipstick level above refill mark | S11 | C |
| 7 | Power steering pump | Belt snug (up to ~3/4 in. play at center), no cracks or frays — or gear-driven: operating, not leaking, secure | S11 | C |
| 8 | Water pump | Same belt/gear checks; not leaking | S11 | C |
| 9 | Alternator | Same belt/gear checks; wiring secure | S11 | C |
| 10 | Air compressor (`AIR`) | Same belt/gear checks | S11 | C |
| 11 | Steering box | Securely mounted, not cracked or leaking, hardware present | S11, 393.209 | C |
| 12 | Power steering hoses | No leaks; connected at both ends | S11 | C |
| 13 | Steering column/shaft | Not bent, loose, or missing parts | S11, 393.209 | P |
| 14 | Pitman arm | Not worn, cracked, or loose | S11, 393.209 | C |
| 15 | Drag link | Not worn, cracked, or loose; joints/sockets tight | S11, 393.209 | C |
| 16 | Tie rod | Not worn, cracked, or loose | S11, 393.209 | C |
| 17 | Castle nuts and cotter pins | Present and secure across the steering linkage | S11 | C |

### 2. Steer axle — suspension, brakes, wheel (items 18–32)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 18 | Front leaf springs | Not shifted or scissoring; none broken or missing; seated in hangers | S11, 393.207 | C |
| 19 | Front spring mounts/hangers | Not cracked or broken; bushings present; bolts tight, none missing | S11, 393.207 | C |
| 20 | Front U-bolts | Not cracked, broken, or loose; hardware present | S11 | C |
| 21 | Front shock absorbers | Secure, not leaking, not bent | S11 | C |
| 22 | Front brake hoses/lines (`AIR`) | Not cracked, worn, or leaking; couplings tight | S11, 393.45 | C |
| 23 | Front brake chamber (`AIR`) | Not leaking, cracked, or dented; securely mounted | S11 | C |
| 24 | Front slack adjuster and push rod (`AIR`) | With brakes released, push rod moves no more than about 1 in. when pulled by hand (manual adjusters) | S11 | C |
| 25 | Front brake drum | No cracks, dents, or holes; no loose or missing bolts; no oil/grease contamination | S11, 396A | C |
| 26 | Front brake linings | Not dangerously thin; visible lining where inspection opening allows (periodic-inspection floor: 1/4 in. at drum-brake shoe center, 396A) | S11, 396A | C |
| 27 | Steer tire tread and condition | Tread ≥ 4/32 in. in every major groove (§ 393.75(b)), evenly worn, no cuts or sidewall damage | S11, 393.75 | C |
| 28 | Steer tire inflation | Checked with a tire gauge, at proper pressure | S11 | C |
| 29 | Valve stem and cap | Present, not broken or damaged | S11 | C |
| 30 | Front rim | No cracks, bends, or welding repairs | S11, 393.205 | C |
| 31 | Front lug nuts | All present and tight; no rust trails, shiny threads, or cracks at bolt holes | S11, 393.205 | C |
| 32 | Front hub oil seal | Not leaking; oil level adequate if sight glass present | S11 | C |

### 3. Cab check and engine start (items 33–46)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 33 | Safety belt | Securely mounted; adjusts and latches properly | S11, 392.7-adjacent (393.93) | P |
| 34 | Safe start | Gearshift in neutral (park if automatic), clutch depressed; start engine, release clutch slowly | S11 | C |
| 35 | Oil pressure gauge | Rises to normal (or warning light goes off) within seconds | S11 | C |
| 36 | Coolant temperature gauge | Climbs gradually to normal operating range | S11 | C |
| 37 | Air gauges (`AIR`) | Pressure builds toward governor cut-out (~120–140 psi or per manufacturer) | S11 | C |
| 38 | Ammeter/voltmeter | Shows alternator charging (or warning light off) | S11 | C |
| 39 | Steering wheel play | No more than about 10 degrees (~2 in. at the rim of a 20-in. wheel) before the front wheel barely moves | S11/S2, 393.209 | C |
| 40 | Mirrors | Clean and properly adjusted from the driver's seat | S11, 392.7 | C |
| 41 | Windshield | Clean; no illegal stickers, obstructions, or glass damage | S11, 393 Subpart D | C |
| 42 | Windshield wipers | Arms and blades secure, undamaged, operate smoothly | S11, 392.7 | C |
| 43 | Windshield washers (`IF-EQUIPPED`) | Operate correctly | S11 | C |
| 44 | Horn(s) | Electric and/or air horn work | S11, 392.7 | C |
| 45 | Heater/defroster | Both work | S11 | C |
| 46 | Dash indicators | Left/right turn, four-way flasher, and high-beam indicators work | S11 | C |

### 4. Emergency equipment (items 47–49)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 47 | Fire extinguisher | Present, securely mounted, properly charged and rated (5 B:C, or two 4 B:C; UL 10 B:C if placarded hazmat) | S11, 393.95 | C |
| 48 | Spare electrical fuses | At least one per type/size if the vehicle uses fuses | S11, 393.95 | C |
| 49 | Warning devices | Three bidirectional reflective triangles (FMVSS No. 125); regulation alternatives include at least six fusees | S11, 393.95 | C |

### 5. External light check — full combination (items 50–59)

Perform with help of the dash switches; walk the full vehicle for each function.

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 50 | Headlights — low beam | Both operate, clean, aimed | S11, 393.9, 392.7 | C |
| 51 | Headlights — high beam | Both operate | S11, 393.9 | C |
| 52 | Turn signals | Front, side, and rear (tractor and trailer) flash correctly left and right | S11, 393.9 | C |
| 53 | Four-way flashers | All flash, front and rear | S11, 393.9 | C |
| 54 | Brake lights | Come on with pedal, release cleanly (tractor and trailer) | S11, 393.9 | C |
| 55 | Taillights | On, clean, red | S11, 393.9 | C |
| 56 | Backing lights | Operate in reverse | S11 | C |
| 57 | Clearance/marker/ID lights — tractor | All operate; amber forward, red rear | S11, 393.9 | C |
| 58 | Clearance/marker/ID lights — trailer (`COMBO`) | All operate; correct colors | S11, 393.9 | C |
| 59 | Reflectors/conspicuity | Clean, unbroken, none missing; correct colors — reflex reflectors amber at the front and front-side, **red at the rear and rear-side**; conspicuity sheeting is a **red-and-white** pattern, not amber (§ 393.11 reflector color table; § 393.13 conspicuity) | S11, 393.11/.13 | C |

### 6. Side of tractor (items 60–66)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 60 | Door(s) and hinges | Not damaged; open, close, and latch properly from outside | S11 | C |
| 61 | Mirror brackets | Securely mounted, not damaged, no loose fittings | S11 | C |
| 62 | Fuel tank(s) | Securely mounted; cap(s) tight; no leaks from tank or lines | S11, 393 Subpart E | C |
| 63 | Drive shaft | Not bent or cracked; couplings secure and free of foreign objects | S11 | C |
| 64 | Exhaust system | Connected tightly and mounted securely; no leak signs (rust streaks, carbon soot) | S11, 396A | C |
| 65 | Tractor frame and cross members | No cracks, broken welds, holes, or damage | S11, 393.201 | C |
| 66 | Catwalk and steps | Solid, securely bolted, clear of loose objects | S11 | P |

### 7. Drive axles — suspension, brakes, wheels (items 67–85)

Checked at each drive axle; dual wheels throughout.

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 67 | Leaf/air springs | Not broken, missing, shifted, or scissoring | S11, 393.207 | C |
| 68 | Spring mounts/hangers | Not cracked or broken; bushings intact; bolts present | S11, 393.207 | C |
| 69 | U-bolts | Not cracked, broken, or loose | S11 | C |
| 70 | Torque arm | Secure, bushing intact | S11 | P |
| 71 | Shock absorbers | Secure, not leaking, not bent | S11 | C |
| 72 | Air suspension bags and mounts (`IF-EQUIPPED`) | Not split or cut; no audible leaks; mounts secure, hardware present | S11 | C |
| 73 | Brake hoses/lines (`AIR`) | Not cracked, worn, or leaking | S11, 393.45 | C |
| 74 | Brake chambers (`AIR`) | Not leaking, cracked, dented; secure | S11 | C |
| 75 | Slack adjusters and push rods (`AIR`) | ≤ ~1 in. push-rod travel pulled by hand, brakes released | S11 | C |
| 76 | Brake drums and linings | Drums free of cracks/holes/contamination; linings not dangerously thin | S11, 396A | C |
| 77 | Drive tire tread and condition | Tread ≥ 2/32 in. in every major groove (§ 393.75(c)), even wear, no cuts/damage | S11, 393.75 | C |
| 78 | Drive tire inflation | Gauge-checked, proper pressure | S11 | C |
| 79 | Valve stems and caps | Present, undamaged | S11 | C |
| 80 | Spacers/Budd spacing | Not bent, damaged, or rusted through; duals evenly separated and centered | S11 | C |
| 81 | Between the duals | No stones, debris, or contact between tires | S11 | P |
| 82 | Rims | No cracks, bends, or welding repairs | S11, 393.205 | C |
| 83 | Lug nuts | All present and tight; no looseness indicators | S11, 393.205 | C |
| 84 | Axle/hub seals | Not leaking | S11 | C |
| 85 | Splash guards/mud flaps — tractor (`IF-EQUIPPED`) | Not damaged, mounted securely | S11 | C |

### 8. Coupling system (items 86–97, all `COMBO`)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 86 | Air lines to trailer (`AIR`) | Listen for leaks; not cut, chafed, spliced, or worn (no steel braid showing); not tangled, pinched, or dragging; secure at both ends | S11 | C |
| 87 | Glad hands | Locked in place, undamaged, not leaking | S11 | C |
| 88 | Electrical cord | Firmly seated and locked in place | S11 | C |
| 89 | Fifth wheel platform | No cracks or breaks in the platform structure | S11, 393.70 | C |
| 90 | Fifth wheel skid plate | Securely mounted; properly lubricated | S11 | C |
| 91 | Fifth wheel mounting bolts/pins | All secure, none missing | S11, 393.70 | C |
| 92 | Release arm and safety latch | Release arm engaged; safety latch in place | S11 | C |
| 93 | Locking jaws | Viewed in the fifth-wheel gap: jaws fully closed **around the kingpin** (not the head) | S11, 393.70 | C |
| 94 | Apron | Not bent, cracked, or broken; lying flat on the skid plate — **no gap** | S11 | C |
| 95 | Kingpin | Not bent, worn, or damaged | S11 | P |
| 96 | Sliding fifth wheel locking pins (`IF-EQUIPPED`) | Present, not loose or missing, fully engaged | S11 | C |
| 97 | Slider position | Positioned so the tractor frame clears the landing gear and the cab clears the trailer during turns | S11 | C |

### 9. Trailer (items 98–121, all `COMBO`)

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 98 | Header board / front wall (`IF-EQUIPPED`) | Secure, undamaged, strong enough to contain cargo | S11, 393.106 | C |
| 99 | Trailer air/electrical connections | Glad-hand and plug receivers secure, sealing, undamaged | S11 | P |
| 100 | Landing gear | Fully raised; no missing parts; crank handle secured; support frame undamaged | S11 | C |
| 101 | Trailer frame and cross members | No cracks, broken welds, holes; box and floor sound | S11, 393.201 | C |
| 102 | Tandem release / locking pins (`IF-EQUIPPED`) | Locking pins locked in place; release arm secured | S11 | C |
| 103 | Trailer leaf/air springs | Not broken, missing, or shifted | S11, 393.207 | C |
| 104 | Trailer spring mounts/hangers | Intact, secure, hardware present | S11, 393.207 | C |
| 105 | Trailer shocks / air bags and mounts (`IF-EQUIPPED`) | Secure, undamaged, no leaks | S11 | C |
| 106 | Trailer brake hoses/lines (`AIR`) | Not cracked, worn, or leaking | S11, 393.45 | C |
| 107 | Trailer brake chambers (`AIR`) | Not leaking, cracked, dented; secure | S11 | C |
| 108 | Trailer slack adjusters (`AIR`) | ≤ ~1 in. push-rod travel by hand, brakes released | S11 | C |
| 109 | Trailer brake drums and linings | No cracks/holes/contamination; linings not dangerously thin | S11, 396A | C |
| 110 | Trailer tire tread and condition | Tread ≥ 2/32 in., even wear, no cuts/damage | S11, 393.75 | C |
| 111 | Trailer tire inflation | Gauge-checked | S11 | C |
| 112 | Valve stems and caps | Present, undamaged | S11 | C |
| 113 | Spacers/dual spacing | Undamaged, evenly centered | S11 | C |
| 114 | Between the duals | Nothing lodged; no tire contact | S11 | P |
| 115 | Trailer rims | No cracks, bends, illegal welds | S11, 393.205 | C |
| 116 | Trailer lug nuts | All present, tight, no looseness indicators | S11, 393.205 | C |
| 117 | Trailer hub/axle seals | Not leaking | S11 | C |
| 118 | Doors, hinges, latches | Not damaged; open, close, and latch properly; seals intact | S11 | C |
| 119 | Ties, straps, chains, binders | Cargo securement devices secure and undamaged | S11, 393 Subpart I | C |
| 120 | Cargo lift (`IF-EQUIPPED`) | No leaking, damaged, or missing parts; fully retracted and latched | S11 | C |
| 121 | Splash guards — trailer (`IF-EQUIPPED`) | Not damaged, mounted securely | S11 | C |

### 10. Brake checks — performed last (items 122–131)

**Safety prerequisite (applies to this whole section):** before releasing the parking
brakes for any engine-off leakage or pop-out test, chock the wheels or otherwise secure the
vehicle against rolling. Several of these tests release the service and/or parking brakes
with the engine off; on any grade an unsecured tractor-trailer can roll. Perform brake tests
on level ground where possible, wheels chocked.

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 122 | Air build-up rate (`AIR`) | Pressure builds within the normal time/range (commonly stated as 85→100 psi within 45 s; **figure needs verification against manual Section 5/11**) | S11/S5 | U |
| 123 | Governor cut-out (`AIR`) | Compressor stops at the manufacturer-set cut-out, commonly ~120–140 psi. There is **no federal maximum** cut-out pressure — § 393.51 governs only the low-air warning and gauge, not cut-out — so a high-but-in-spec cut-out is not itself a defect | S11 | C |
| 124 | Governor cut-in (`AIR`) | Compressor restarts at the specified lower pressure (**figure needs verification**) | S5 | U |
| 125 | Static leakage test (`AIR`) | Engine off, brakes released: pressure loss ≤ ~2 psi/min single vehicle, ≤ ~3 psi/min combination (standard manual figures — distinct from the applied-test figures below; **exact Section 11/5 wording pending full-text verification**) | S11/S5 | U |
| 126 | Applied leakage test (`AIR`) | Engine off, service brake fully applied: loss ≤ ~3 psi/min single vehicle, ≤ ~4 psi/min combination | S5 | C |
| 127 | Low air pressure warning (`AIR`) | Warning activates **before the pressure drops below 60 psi** during pump-down | S11/S5 | C |
| 128 | Spring / emergency brake pop-out (`AIR`) | Continue the pump-down: the tractor parking-brake (spring) valve — and, on a combination, the trailer air-supply valve / tractor-protection valve — must close automatically in the **20–45 psi** range, applying the parking/emergency brakes | S11/S5 | C |
| 129 | Tractor parking brake test | With wheels chocked removed only after this test: parking brake set (trailer brakes released), gently pull forward against the tractor parking brake — it must hold | S11 | C |
| 130 | Trailer brake test (`COMBO`) | **Tractor parking brake released**, trailer brakes set (trailer air-supply/hand valve applied): gently pull forward against the trailer brakes alone — they must hold (releasing the tractor brake first is what makes this test the trailer's, not the tractor's) | S11 | P |
| 131 | Service brake check | With both parking brakes released, roll at ~5 mph and apply the service brake: firm stop, no pulling to either side, no delay | S11 | P |

Hydraulic-brake variant (replaces the `AIR` items above): pump the pedal three times, apply
firm pressure, hold five seconds — the pedal must not depress; failure is disqualifying on
the state test (corroborated: GA, ID, FL Section 11 materials).

## Verification ledger

- **Corroborated (C):** 118 items — matched to official state republications of Section 11
  and/or exact CFR text via search excerpts (multiple states cross-checked; identical
  AAMVA-derived wording confirmed in MI, NY, PA, GA, WA copies).
- **Partial (P):** 10 items (13, 33, 66, 70, 81, 95, 99, 114, 130, 131) — standard Section
  11 content echoed by secondary sources; official wording not yet captured in excerpts.
- **Unverified figures (U):** 3 items (122, 124, 125) — figures not yet captured from the
  official full text; must be read from the manual/regulation before shipping.
- **Before any of this ships:** a line-by-line pass against the FMCSA-hosted model manual
  PDF (July 2014) and eCFR full text in a network-capable session, resolving every P and U;
  then the existing human review gates (foundation approval, content import approval,
  safety wording review).

### CVSA OOSC coverage cross-check (owner-supplied 2004 edition, reference only)

The owner provided a January 1, 2004 CVSA Out-of-Service Criteria scan (see
`docs/content-sourcing.md` for its licensing boundaries — reference only, not committed,
never a source of shipped text or figures). Its Part II vehicle categories were
cross-checked against this checklist at the table-of-contents level:

- **Covered by the 131 items:** brake systems (drums, linings, hoses, low-pressure warning,
  air loss, parking brakes, compressor), coupling (fifth wheel, upper/lower coupler),
  exhaust, frame, fuel system, lamps/turn signals, safe loading/tie-downs, steering
  (free play, column, gear box, pitman arm, tie rod/drag links, nuts), suspension (spring
  assemblies, air suspension, torque/tracking components), tires (steer vs other), wheels/
  rims/hubs, windshield wipers.
- **OOSC areas out of this checklist's scope:** enforcement-only measures (brake
  adjustment-limit charts), other configurations (pintle hooks, drawbar/tongue,
  saddlemounts, electric/vacuum brakes, van body rails as separate items), and buses
  (emergency exits) — bus items belong to the future Passenger/School Bus content, not the
  Class A checklist.
- **Verification-pass candidates raised by the cross-check:** the OOSC lists the
  *tractor-protection system* and *air reservoir* as distinct brake-system areas. Item 128
  (reworded above) now covers the tractor-protection/trailer-air-supply valve *pop-out*
  during pump-down. The full-text pass must confirm Section 11's exact wording for the
  air-reservoir/air-tank check and fold it into the compressor items if named.

### Known gap flagged in review (open decision for the verification pass)

A combination-vehicle **trailer air-supply / breakaway test is not yet a distinct item** and
must not be treated as already covered: item 128 tests that the valves *pop out* on
pump-down, but it does not exercise the full sequence of charging the trailer, then pulling
the trailer air-supply knob and confirming the **trailer emergency (breakaway) brakes
actually apply**. That is a genuinely separate combination brake-safety check. Resolving it
during the full-text verification pass is likely to **add one item (making the count 132)**
or require merging two granular items to hold at 131. That count change is a content-
accuracy decision for the human review gate, not something to resolve silently here — the
honest position for now is that the check is *missing*, not *included*.

## App-mapping notes

- `InspectionCategory` maps to the ten sections above; `InspectionItem.inspectFor` carries
  the check text; `sampleDefects` carries defect examples drawn from Section 11 "check for"
  language and Appendix A criteria. All current in-app content is fictional sample data and
  stays labeled until this checklist passes review.
- Needed schema additions before import (Room migration — review required): per-item source
  citation, verification date/status, and applicability flags (`COMBO`/`AIR`/`IF-EQUIPPED`)
  so Real Inspection Mode can filter by the driver's configuration honestly.
- Real Inspection Mode order = item order above (brake checks last, before rolling).
  Study Mode may group by section with the same numbering.
