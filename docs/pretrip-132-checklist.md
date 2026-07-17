# 132-Point Pre-Trip Inspection — Researched Definition

This document defines Last Wagon's 132-point commercial-vehicle pre-trip inspection:
scope, sequence, every item, what to check, applicability, and the authoritative source
basis for each. It implements the blueprint requirement to research and verify the
checklist against authoritative sources before implementation. Licensing and authoring
rules are in `docs/content-sourcing.md`. This is a content definition for human review —
it does not authorize importing content into the app.

## What "132 points" means — required honesty

Research found **no official source that defines a fixed-point pre-trip inspection** (131,
132, or any other count). It is not a term used by FMCSA, AAMVA, any state CDL manual, or
any regulation. Official material instead defines inspection *areas* and *items*: the CDL
manual's Section 11 walk-around and the parts lists of 49 CFR 392.7, 393, and 396. Item
counts in official and training material vary with enumeration granularity (roughly 60–150
for a tractor-trailer).

Therefore: **132 is Last Wagon's product enumeration** — a specific, fixed way of counting
the authoritative inspection content for a Class A tractor + semi-trailer with air brakes.
The app must present it as "Last Wagon's 132-point checklist, built from the CDL manual
inspection test and federal regulations," and must never call it an official standard,
an official count, or a guarantee of test coverage or legal compliance.

Point-count history (owner decisions, 2026-07-16): the count began as a working "131" (with
a fallback offer of 100/101 that was never needed). During PR review a genuinely missing
combination safety check — the trailer air-supply/breakaway test — was identified; the owner
approved adding it as a distinct item (now item 129), which moved the count to **132**.
Because no count is official, this was an accuracy decision, not a branding one: the number
simply reflects however many verified items the enumeration contains.

## Scope and vehicle applicability

- **132 is the full enumeration, not a per-vehicle count.** The 132 items are the complete
  set for a fully-equipped Class A tractor + single semi-trailer with air brakes. The number
  of items that actually *apply* to a given vehicle is always ≤ 132 and depends on its
  equipment — a leaf-spring tractor with no cargo lift, no sliding fifth wheel, and no
  washers has fewer applicable items than one with all of those. Real Inspection Mode shows
  the applicable count for the driver's configuration; the app must not imply every vehicle
  has 132 live points.
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

## The 132 items

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

### 10. Brake checks — performed last (items 122–132)

**Safety prerequisite (applies to this whole section):** do these on level ground.
- **Engine-off leakage and pop-out tests (122–129):** the service brakes are released or
  the system is pumped down with the engine off, so **chock the wheels first** — on any
  grade an unsecured tractor-trailer can roll.
- **Holding / pull tests (130–132):** these require the vehicle to be able to move, so
  **remove the chocks immediately before each controlled pull test**, keep the vehicle on
  level ground, and be ready to stop — the brake *being tested* is what must hold the
  vehicle. Re-chock afterward if further engine-off work follows.

| # | Item | Check | Source | Status |
|---|---|---|---|---|
| 122 | Air build-up rate (`AIR`) | Pressure builds within the normal time/range (commonly stated as 85→100 psi within 45 s; **figure needs verification against manual Section 5/11**) | S11/S5 | U |
| 123 | Governor cut-out (`AIR`) | Compressor stops at the manufacturer-set cut-out, commonly ~120–140 psi. There is **no federal maximum** cut-out pressure — § 393.51 governs only the low-air warning and gauge, not cut-out — so a high-but-in-spec cut-out is not itself a defect | S11 | C |
| 124 | Governor cut-in (`AIR`) | Compressor restarts at the specified lower pressure (**figure needs verification**) | S5 | U |
| 125 | Static leakage test (`AIR`) | Engine off, brakes released: pressure loss ≤ ~2 psi/min single vehicle, ≤ ~3 psi/min combination (standard manual figures — distinct from the applied-test figures below; **exact Section 11/5 wording pending full-text verification**) | S11/S5 | U |
| 126 | Applied leakage test (`AIR`) | Engine off, service brake fully applied: loss ≤ ~3 psi/min single vehicle, ≤ ~4 psi/min combination | S5 | C |
| 127 | Low air pressure warning (`AIR`) | Warning activates **before the pressure drops below 60 psi** during pump-down | S11/S5 | C |
| 128 | Spring / emergency brake pop-out (`AIR`) | Continue the pump-down: the tractor parking-brake (spring) valve — and, on a combination, the trailer air-supply valve / tractor-protection valve — must close automatically in the **20–45 psi** range, applying the parking/emergency brakes | S11/S5 | C |
| 129 | Trailer air-supply / breakaway test (`AIR`/`COMBO`) | With the system fully charged, pull the trailer air-supply (red) knob out: the tractor-protection / trailer-air-supply valve must close and the trailer **emergency (breakaway) brakes must apply and lock**. This is a distinct combination check from the trailer service-brake hold test below | S11 | P |
| 130 | Tractor parking brake test | Chocks removed; parking brake set (trailer brakes released): gently pull forward against the tractor parking brake — it must hold | S11 | C |
| 131 | Trailer brake test (`COMBO`) | **Tractor parking brake released**, trailer brakes set (trailer hand/trolley valve applied): gently pull forward against the trailer brakes alone — they must hold (releasing the tractor brake first is what makes this the trailer's test, not the tractor's) | S11 | P |
| 132 | Service brake check | With both parking brakes released, roll at ~5 mph and apply the service brake: firm stop, no pulling to either side, no delay | S11 | P |

Hydraulic-brake variant (replaces the `AIR` items above): pump the pedal three times, apply
firm pressure, hold five seconds — the pedal must not depress; failure is disqualifying on
the state test (corroborated: GA, ID, FL Section 11 materials).

## Verification ledger

- **Corroborated (C):** 118 items — matched to official state republications of Section 11
  and/or exact CFR text via search excerpts (multiple states cross-checked; identical
  AAMVA-derived wording confirmed in MI, NY, PA, GA, WA copies).
- **Partial (P):** 11 items (13, 33, 66, 70, 81, 95, 99, 114, 129, 131, 132) — standard
  Section 11 content echoed by secondary sources; official wording not yet captured in
  excerpts. (Item 129, the trailer air-supply/breakaway test, was added 2026-07-16 and is
  core combination content, but its exact Section 11 wording is still pending capture.)
- **Unverified figures (U):** 3 items (122, 124, 125) — figures not yet captured from the
  official full text; must be read from the manual/regulation before shipping.
- **Total: 132 items** (118 C + 11 P + 3 U).
- **Before any of this ships:** a line-by-line pass against the FMCSA-hosted model manual
  PDF (July 2014) and eCFR full text in a network-capable session, resolving every P and U;
  then the existing human review gates (foundation approval, content import approval,
  safety wording review).

### CVSA OOSC coverage cross-check (owner-supplied 2004 edition, reference only)

The owner provided a January 1, 2004 CVSA Out-of-Service Criteria scan (see
`docs/content-sourcing.md` for its licensing boundaries — reference only, not committed,
never a source of shipped text or figures). Its Part II vehicle categories were first
cross-checked at the table-of-contents level; on **2026-07-17** the full Part II text
(14 vehicle sections, OCR of the public.resource.org incorporated-by-reference copy) was
read item-by-item against the 132 items, with currency questions checked against 2026
sources by web search. Results:

**Coverage at item level.** With the exception of the gap candidates listed below (most
notably the front axle beam and the air reservoir, which are driver-detectable and NOT
covered by any current item), every driver-detectable OOSC Part II defect area maps
to one or more of the 132 items: brake components/linings/drums/hoses/tubing (22–26,
73–76, 86, 106–109), low-air warning (127), applied air-loss (126), tractor-protection
system (128–129), compressor (10), coupling incl. sliders/latching/kingpin/apron (86–97),
exhaust (64), frame incl. sliding-tandem locking pins (65, 101–102), fuel (62), lighting
(50–59), tie-down device condition (98, 119), steering free play/column/gear box/pitman/
links/nuts (11–17, 39), suspension incl. torque and air components (18–21, 67–72,
103–105), tires (27–29, 77–81, 110–114), van-body floor/rails at driver level (101),
wheels/rims/hubs/fasteners/welds (30–32, 82–84, 115–117), wipers (42).

**Direct validations from the item-level pass:**

- **Item 129 confirmed:** OOSC brake §1.c(1) treats an inoperable trailer *breakaway*
  system as its own condition (no trailer brake application when the supply is actuated) —
  exactly the check item 129 added in PR review. The addition closed a real gap.
- **Item 127 figures reconciled:** current 49 CFR 393.51 (exact sentence captured via
  eCFR-derived excerpt, 2026-07-17: "at 379 kPa (55 psi) and below, or one-half of the
  compressor governor cutout pressure, **whichever is less**") — the 2004 OOSC §1.g uses
  the same "whichever is less" wording. A PR-review bot suggested the regulation reads
  "whichever is greater"; two independent sources contradict that, so "less" stands here,
  but the phrase is explicitly on the full-text verification pass with the other 393.51
  wording. The CDL manual's driver-facing "before 60 psi" is the stricter study figure;
  authoring presents 60 as the manual's check figure and may cite 393.51 in the
  explanation.
- **Tread-depth figures verified current** (eCFR § 393.75 excerpt, 2026-07-17): 4/32 steer
  (item 27), 2/32 others (77, 110). The OOSC's lower floors (2/32 steer; 1/32 others in
  two adjacent grooves, 3 locations) are enforcement floors *below* the legal maintenance
  minimums — useful contrast for defect-example text, cited to § 393.75, not the OOSC.
- **Enforcement-only areas confirmed out of scope:** the 20 % defective-brake criterion,
  pushrod-stroke adjustment-limit charts by chamber type, steering free-play degree charts
  (OOS at 30°/45° vs the manual's ~10° driver standard), fastener-count thresholds, and
  coupler-movement measurements are roadside-measurement standards, not driver walk-around
  checks. Other-configuration areas (pintle/drawbar/saddlemount, electric/vacuum brakes,
  gaseous fuels, C-dollies, bus exits) stay with their future content categories.

**Gap and enhancement candidates raised (owner decisions at the verification pass):**

1. **Front axle beam** — OOSC steering §8.c lists front-axle-beam cracks/welded repairs,
   and state Section 11 materials confirm "the axle itself should not be cracked" (web
   excerpts, 2026-07-17). The 132 items cover the steer axle's springs/brakes/wheel but
   never the axle beam itself. Options: new item (count → 133) or fold into the steer-axle
   section's inspect-for text.
2. **Air reservoir/tank security and drain** — OOSC §1.j (reservoir separated from
   attachment points); reaffirms the earlier TOC-level flag. Confirm Section 11/5 wording
   at the full-text pass; likely a new item or a compressor-item extension.
3. **ABS malfunction lamps** — post-dates the walk-around lists this checklist was built
   from but is current law (49 CFR 393.55, verified 2026-07-17): tractors and trailers
   since ~1998 carry malfunction lamps, incl. the **external left-rear "ABS" lamp on
   trailers**; the standard driver check is lamp-on-then-off at key-on. Absent from the
   132. Candidates: extend item 46 (dash indicators) and item 99/trailer section, or add
   dedicated items.
4. **Disc brakes** — items 25/76/109 name drums only; § 393.47 and the OOSC cover rotors
   (air-disc pad floor 1/8 in.). Authoring should say "drums or rotors" with the matching
   figures where applicable.
5. **Automatic slack adjusters** — items 24/75/108 qualify the 1-inch hand check as
   "manual adjusters." Current guidance (verified 2026-07-17): on ASAs the driver still
   *checks* travel but must never manually adjust — an out-of-adjustment ASA is a
   maintenance defect to report. Fold into authoring wording.
6. **Minor defect-example enrichments:** refrigeration/heater fuel-system leaks under
   item 62 (OOSC fuel §5.a covers them); tire/body contact under load (OOSC frame §4.b)
   as a loaded-vehicle caution; projecting-load lamp rules belong to future cargo content.

**Currency caveat.** The 2004 scan is 22 annual revisions old: the OOSC is reissued every
April 1, and the 2026 edition (effective 2026-04-01, 17 changes incl. brake-measurement,
cargo-securement, and wheels/rims updates) is current. The scan therefore remains a
*category-coverage* reference only; every figure or rule that ships must be verified
against current public-domain sources (eCFR Part 393/396 Appendix A), never against the
scan. No OOSC text or figures ship in the app.

### Resolved: trailer air-supply / breakaway test added (item 129)

A combination-vehicle **trailer air-supply / breakaway test** — charge the system, pull the
trailer air-supply (red) knob, and confirm the trailer emergency (breakaway) brakes actually
apply — was flagged in review as missing (item 128 only tests that the valves *pop out* on
pump-down). The owner approved adding it as a distinct item (2026-07-16), which is why the
count is **132, not 131**. Its exact Section 11 wording is still pending the full-text
verification pass (status P).

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
