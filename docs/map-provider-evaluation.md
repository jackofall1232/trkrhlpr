# Map Provider Evaluation

## Phase 1 evaluation provider (kept for tests only)

Phase 1 used `https://demotiles.maplibre.org/style.json` only as token-free development
and evaluation data. It is the style used by MapLibre's Android quickstart and is hosted by
the MapLibre project for examples and CI tests. The demo repository is BSD-3-Clause and
describes the lightweight world polygons as Natural Earth data.

References:

- [MapLibre Android quickstart](https://maplibre.org/maplibre-native/android/examples/getting-started/)
- [MapLibre demo tiles repository](https://github.com/maplibre/demotiles)

The demo endpoint must never be used for corridor prefetching; its
`MapStyleDescriptor.offlinePrefetchPermitted` flag is `false` and the corridor manager
refuses to prefetch from any provider whose flag is `false`.

## Phase 5 development corridor provider — OpenFreeMap

Phase 5 requires "a provider that permits offline prefetching". The development and
testing provider selected for corridor downloads is **OpenFreeMap**
(`https://tiles.openfreemap.org/styles/liberty`), based on the following recorded terms
research (2026-07-16, via web search; direct site fetch was unavailable in the work
environment):

- The public instance is free for commercial and non-commercial use, with **no
  registration, no API keys, and no stated limits on the number of map views or
  requests**.
- Required attribution is "OpenFreeMap © OpenMapTiles, Data from OpenStreetMap"; the
  underlying data is OpenStreetMap (ODbL) rendered as OpenMapTiles-schema vector tiles.
- OpenFreeMap publishes weekly **full-planet downloads (Btrfs and MBTiles)** and
  self-hosting documentation as its sanctioned bulk-transfer channel, so corridor
  prefetching from the public CDN must stay modest.
- An explicit written clause about mobile offline region prefetching from the public
  instance was **not** located during this research pass. This is recorded as an open
  item, not as permission.

Sources: [openfreemap.org](https://openfreemap.org/),
[OpenFreeMap on GitHub](https://github.com/hyperknot/openfreemap),
[Simon Willison's OpenFreeMap notes](https://simonwillison.net/2024/Sep/28/openfreemap/).

### Safeguards applied in the implementation

- Corridor downloads are bounded by a hard tile-count cap (`OfflineCorridor.MAX_TILES`)
  and a small zoom range chosen per detail level, keeping a single route corridor in the
  low thousands of tiles at most — interactive-viewing magnitude, not bulk mirroring.
- Only one corridor may exist at a time; replacing or deleting a route deletes its
  corridor.
- Attribution is displayed on the map screen for the active style provider.
- The provider remains replaceable behind `MapStyleProvider`, and prefetch permission is
  a per-provider flag, so a keyed commercial provider or a self-hosted OpenFreeMap
  instance can be substituted without touching corridor logic.

### Human review gate before production

This selection is for development and representative-device testing of Phase 5. Before
any production release: confirm OpenFreeMap public-instance terms in writing (or move to
self-hosting/a commercial plan), re-verify attribution requirements, and record the
outcome here. Production tile-provider selection remains a human review gate per the
blueprint and `truck-routing-plan.md` Phase 5 exit criteria ("provider terms are
satisfied").
