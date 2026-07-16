# Phase 1 Map Provider Evaluation

Phase 1 uses `https://demotiles.maplibre.org/style.json` only as token-free development
and evaluation data. It is the style used by MapLibre's Android quickstart and is hosted by
the MapLibre project for examples and CI tests. The demo repository is BSD-3-Clause and
describes the lightweight world polygons as Natural Earth data.

References:

- [MapLibre Android quickstart](https://maplibre.org/maplibre-native/android/examples/getting-started/)
- [MapLibre demo tiles repository](https://github.com/maplibre/demotiles)

The UI labels this source as an evaluation map and displays attribution. It is not a
commercial-truck map, production tile-provider selection, or evidence that a road is legal,
safe, or passable. Phase 1 performs normal interactive viewing only and implements no tile
prefetching, offline region download, route calculation, or background location collection.

Before production use or Phase 5 corridor prefetching, select a provider through a separate
review covering licensing, attribution, availability, privacy, caching, offline storage,
download limits, coverage, data freshness, and commercial-vehicle restriction data. The
`MapStyleProvider` interface keeps that selection replaceable.
