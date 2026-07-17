# Truck-Stop Directory — Public Data-Source Research (Track C, unit 10)

This document records the researched public data sources for Last Wagon's future
truck-stop directory (Track C): what each source contains, who publishes it, what its
license permits, how fresh it is, and whether it can be stored offline inside a sideloaded
Android app. It ends with a recommended sourcing approach and the verification worklist
that must be cleared before implementation.

Research method and limits: compiled 2026-07-17 in a remote session whose network policy
blocked **all** direct page fetches (proxy CONNECT denied with 403 for every candidate
host probed: geodata.bts.gov, bts.gov, catalog.data.gov, ecfr.gov, govinfo.gov,
wiki.openstreetmap.org, overpass-api.de, opendatacommons.org, overturemaps.org,
ops.fhwa.dot.gov). Only web search with source excerpts worked. Every claim below is
corroborated by search excerpts attributed to the cited official URL; nothing here was
verified against full source text. Claims whose exact wording matters (license statements,
field schemas) are marked **UNVERIFIED** and sit in the verification worklist (§ 7).
Nothing in this document authorizes implementation; the Track C source-approval human
gate (§ 8) still applies.

## 1. What the directory needs from a data source

From the blueprint (future-roadmap directory of truck-accessible stops, fuel, parking,
rest areas, services, amenities) and the confirmed offline-first constraint:

- **Locations**: name, coordinates, state/highway context for US truck stops, travel
  plazas, and public rest areas that accommodate commercial vehicles.
- **Truck-relevant attributes**: truck parking capacity, diesel/DEF availability,
  showers, food, repair — as available; the directory must degrade gracefully when an
  attribute is unknown (unknown ≠ absent, same rule as routing's "missing data is
  unknown").
- **License permitting**: offline storage on-device, redistribution inside a sideloaded
  APK, and modification (schema mapping, deduplication) — verified *before* adoption, per
  `.l00prite/constraints.md`.
- **Freshness story**: a documented vintage per import and a feasible refresh path.
  Truck stops open, close, and change; a static snapshot must be labeled as such in-app.

## 2. Licensing landscape — candidate classes

| Source class | Status | Use in Last Wagon |
|---|---|---|
| USDOT/BTS NTAD datasets (federal geospatial data) | US government works are public domain (17 U.S.C. § 105), **but NTAD licensing is per-dataset** — e.g. the Intercity Bus Atlas inside NTAD is CC-BY-NC-4.0. The Truck Stop Parking dataset's own license statement is **UNVERIFIED** | Strong primary candidate. Offline storage and APK redistribution are unproblematic *if* the per-dataset statement confirms public domain / no restriction. Verify first (§ 7 V1). |
| OpenStreetMap | ODbL 1.0 — attribution + share-alike. Extracting POIs into our own bundled database creates a *Derivative Database* that must itself remain ODbL and be made available in unrestricted form even when shipped inside an app ("technological measures" clause). App code and other content are unaffected if the OSM-derived table is kept a separate component of a *Collective Database* | Usable with a deliberate compliance design: separate ODbL-licensed dataset file/table, visible "© OpenStreetMap contributors, ODbL" attribution where users will look, and a published copy of (or recreation recipe for) the extracted dataset. |
| Overture Maps — Places theme | CDLA-Permissive-2.0 for Meta/Microsoft-contributed place records; obligation is to include the license text, no share-alike, and results/insights derived from the data carry no obligation. Caveat: ~40 % of Overture records overall derive from OSM and recent releases carry per-source license info in the feature `sources` property; any OSM-derived rows would be ODbL. Exact per-record license semantics **UNVERIFIED** | Attractive low-obligation enrichment/conflation source if filtered to CDLA-Permissive records. Category taxonomy fit (truck-stop category name) **UNVERIFIED** (§ 7 V3). |
| TPIMS real-time feeds (MAASTO eight-state system: IN, IA, KS, KY, MI, MN, OH, WI) | Public dynamic JSON feed updated at least every 5 minutes, offered to third-party developers via a published data-exchange spec; each state hosts its own feed. Per-state terms **UNVERIFIED** | Future *online* enhancement only. Real-time availability contradicts the offline-first first milestone and covers only eight states. Out of Track C scope; recorded for the roadmap (§ 6). |
| Proprietary directories (Trucker Path, chain locators — Pilot Flying J, Love's, TA Petro — commercial POI APIs such as Google Places) | Proprietary databases and/or API terms that prohibit bulk export, offline caching, or redistribution | **Excluded.** `.l00prite/constraints.md` forbids assuming access to proprietary truck-stop datasets; scraping chain locators would violate their terms. Style inspiration at most. |

## 3. Primary candidate — USDOT/BTS NTAD "Truck Stop Parking"

- **What it is**: nationwide point dataset of **8,000+ truck parking locations** (public
  rest areas and private truck stops), compiled from state DOTs and private truck-stop
  operators for the FHWA **Jason's Law** Truck Parking Survey mandated by MAP-21
  (PL 112-141). Published by USDOT/BTS in the National Transportation Atlas Database.
- **Access**: https://geodata.bts.gov/datasets/usdot::truck-stop-parking/about (also on
  catalog.data.gov as "Truck Stop Parking" and on data-usdot.opendata.arcgis.com).
  Download formats reported: File Geodatabase, Shapefile, GeoJSON, CSV, KML.
- **Authority**: high — federal statistical agency publication of survey data collected
  under a federal mandate.
- **Freshness**: the dataset was **compiled 2019-04-09** and search evidence shows no
  later compilation. FHWA reissued the Jason's Law survey in 2024 (third round; prior
  rounds 2014 and 2019) with results anticipated afterward, so a refreshed federal
  dataset is plausible but not yet confirmed. Any import must carry a visible
  "data compiled 2019" vintage label until a newer vintage exists.
- **Attributes**: the survey collected location and capacity information; the exact
  attribute schema (parking-space counts, amenity fields, ownership) is **UNVERIFIED**
  because the field-list/API page could not be fetched (§ 7 V1).
- **License**: expected US government work / public domain, **UNVERIFIED** at the
  per-dataset level (see § 2 caveat — NTAD carries at least one CC-BY-NC dataset, so
  "it's in NTAD" is not proof of public domain).
- **Fit**: best available authoritative, likely-unencumbered seed dataset. Matches the
  project's public-domain-first content rule.

## 4. Enrichment candidates

### 4a. OpenStreetMap

- **What it provides**: community-maintained truck-stop and fuel POIs with rich amenity
  tagging. Relevant tagging (per OSM wiki excerpts):
  `highway=services` (motorway service areas / travel plazas / truck stops),
  `amenity=fuel` + `hgv=yes|designated` (truck-accessible fuel),
  `fuel:hgv_diesel=*`, `capacity:hgv=*` (truck parking capacity), plus standard amenity
  tags (showers, toilets, restaurants). US community usage of `highway=services` for
  off-exit truck stops is **debated** (community-forum evidence), so extraction must
  query multiple tag patterns, and coverage/consistency will vary by region.
- **Access**: Geofabrik regional extracts or Overpass API queries; both distribute OSM
  data under ODbL.
- **Freshness**: continuously edited; extracts are current to days or minutes. Refresh =
  re-extract at each content update.
- **License obligations (ODbL 1.0)** — per OSMF Licence/Legal FAQ and Attribution
  Guidelines excerpts:
  1. Attribute "© OpenStreetMap contributors" and link/include the ODbL where users
     would look (app attribution/about screen qualifies; exact placement rules in the
     Attribution Guidelines need full-text review, § 7 V4).
  2. The extracted truck-stop database is a **Derivative Database**: it stays ODbL, and
     because it ships inside an app (a "technological measure"), we must also make it
     available in unrestricted form — practically, publish the extracted dataset (or the
     exact extraction recipe + vintage) alongside each release.
  3. Keep the OSM-derived table a separate Collective-Database component so share-alike
     does not reach Last Wagon's original study content or code.
- **Fit**: best amenity richness and freshness; real but manageable compliance work;
  variable coverage quality. Suitable as enrichment over the federal seed, or as primary
  if the federal license verification fails.

### 4b. Overture Maps — Places theme

- **What it provides**: 59–75 M+ global place records contributed mainly by Meta and
  Microsoft, with a category taxonomy (fuel/gas-station categories confirmed in search
  evidence; presence and name of a dedicated truck-stop category **UNVERIFIED**, § 7 V3).
- **Access**: monthly releases on AWS S3 / Azure Blob, GeoParquet.
- **Freshness**: monthly releases.
- **License**: CDLA-Permissive-2.0 for the Meta/Microsoft place data — include the
  license text, no share-alike, derived "results" unencumbered. Per-record `sources`
  license info exists in recent releases; rows sourced from OSM would instead be ODbL
  (§ 7 V3 verifies the filtering story).
- **Fit**: lowest-obligation bulk POI source if CDLA-only filtering is confirmed;
  category fit for truck stops still unproven. Candidate for conflation/enrichment,
  not yet a committed choice.

## 5. Explicitly excluded sources

- **Trucker Path and similar apps' databases** — proprietary; no lawful bulk access.
- **Chain store locators** (Pilot Flying J, Love's, TA Petro, etc.) — proprietary sites/
  APIs; bulk scraping violates terms. Individual chains' own published developer APIs, if
  any ever permit redistribution, would need their own review.
- **Commercial POI APIs** (Google Places, HERE, TomTom) — terms generally prohibit
  offline bulk storage and redistribution, which the offline-first sideloaded APK
  requires.
- **CAT Scale / weigh-station commercial datasets** — proprietary.

These follow directly from `.l00prite/constraints.md` ("Do not assume access to
proprietary truck-stop, study, or routing datasets") and need no further research unless
the owner obtains a written license from a vendor.

## 6. Deferred: real-time parking availability (TPIMS)

The MAASTO TPIMS gives 5-minute-interval truck-parking availability across eight states
via a published JSON data-exchange spec (V2.2), explicitly offered to third-party
developers; each state hosts its own feed. This is the obvious *future* online
enhancement to a static directory — but it is real-time (useless offline), regional, and
adds per-state terms review. Keep it on the Later roadmap; do not fold it into the
first Track C implementation.

## 7. Verification worklist (blocked-host; needs a less-restricted session or the owner's browser)

| # | Claim to verify | Where | Blocks |
|---|---|---|---|
| V1 | Truck Stop Parking per-dataset license statement, attribute schema, record count, and last-update date | https://geodata.bts.gov/datasets/usdot::truck-stop-parking/about and its API page | Any import of the federal dataset |
| V2 | Whether the 2024 third Jason's Law survey produced (or will produce) a refreshed public location dataset | https://ops.fhwa.dot.gov/freight/infrastructure/truck_parking/ | Choice of vintage to import |
| V3 | Overture Places taxonomy category for truck stops; per-record `sources[].license` semantics for CDLA-only filtering | https://docs.overturemaps.org/guides/places/taxonomy/ and /attribution/ | Any use of Overture |
| V4 | ODbL attribution placement rules for mobile apps and the practical shape of the "unrestricted form" obligation for a bundled extract | https://osmfoundation.org/wiki/Licence/Attribution_Guidelines and Licence/Licence_and_Legal_FAQ | Any use of OSM data |
| V5 | TPIMS per-state feed terms (only if/when the real-time enhancement is pursued) | https://trucksparkhere.com and state DOT pages | TPIMS integration only |

## 8. Recommended approach (proposal — owner approval is the Track C gate)

1. **Seed (Track C unit 11, phase 1):** import the BTS/NTAD Truck Stop Parking dataset,
   after V1/V2 clear, as versioned Room content following the existing labeled-content
   pipeline: per-record provenance (source, dataset vintage, import date), a visible
   dataset-vintage disclaimer, and offline search/filter by name, state, and available
   attributes. No availability, open-now, or amenity claims beyond what the record
   actually carries; unknown attributes display as unknown.
2. **Enrich (phase 2, optional):** add OSM-derived amenity detail under the § 4a
   compliance design (separate ODbL table, attribution screen entry, published extract
   recipe per release), and/or Overture CDLA-filtered records if V3 confirms the
   category fit — whichever the owner prefers after seeing phase-1 gaps.
3. **Defer:** TPIMS real-time availability stays on the Later roadmap (§ 6).
4. **Safety framing:** the directory is informational assistance, not a guarantee that a
   facility exists, is open, has space, or accommodates a specific vehicle — consistent
   with the app-wide "missing data is unknown" rule.

Proposed draft acceptance criteria for unit 11 (to be finalized at approval time):
offline search over the imported dataset; per-record provenance + vintage visible;
attribute filters only over verified fields; explicit unknown-state display; attribution
screen lists every source whose license requires it; dataset replaceable via the
versioned content pipeline without schema migration for a same-shape refresh.

## 9. App-side content schema (phase-2 pipeline, built 2026-07-17)

The app parses **Last Wagon's own truck-stop content JSON** (`TruckStopContent` in
core/model), never a provider format. External datasets are converted to this schema at
content-preparation time, after V1/V2 clear; the app-side pipeline is already live and
carries the labeled sample document (`SampleContent.truckStopsJson`).

Envelope: `schema_version` (currently 1), `dataset` (`citation`, `vintage`,
`verification` = VERIFIED/PARTIAL/UNVERIFIED, `sample` — defaults to `true` so content is
never silently treated as real), `stops`. Per record: `id`, `name`, `state`, `lat`,
`lon` required; `highway`, `parking_spaces`, and amenity booleans (`diesel`, `showers`,
`food`, `repair`) optional — an absent amenity stays unknown (null), never false.
Structurally-broken records are skipped and counted (`skippedRecords`), never fatal, so
an import's coverage is auditable.

Phase-2 remaining work once V1/V2 clear: map the verified NTAD attribute schema to these
fields at content-prep time (mapping is deliberately NOT guessed in code — the field
list is V1 worklist material), produce the real document with
`citation`/`vintage` = the dataset's actual identity and compilation date,
`sample: false`, and replace the sample JSON (bundled asset). No app-schema change is
expected for that swap.
