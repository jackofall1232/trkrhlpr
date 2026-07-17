package com.lastwagon.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwagon.core.designsystem.*
import com.lastwagon.core.model.*

/** Offline truck-stop directory (Track C phase 1). Currently backed by labeled fictional
 *  sample rows only; the verified national dataset import is gated on the source-verification
 *  worklist in docs/truck-stop-data-sources.md. Amenity display follows the app-wide rule
 *  that missing data is unknown, never proof of absence. */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable fun TruckStopsScreen(
    contentRepository: ContentRepository,
    modifier: Modifier = Modifier,
) {
    val stops by contentRepository.observeTruckStops().collectAsStateWithLifecycle(
        initialValue = emptyList(),
    )
    var query by rememberSaveable { mutableStateOf("") }
    var stateFilter by rememberSaveable { mutableStateOf("") }
    var requireDiesel by rememberSaveable { mutableStateOf(false) }
    var requireShowers by rememberSaveable { mutableStateOf(false) }
    var requireFood by rememberSaveable { mutableStateOf(false) }
    var requireRepair by rememberSaveable { mutableStateOf(false) }

    val filter = TruckStopFilter(
        query = query,
        state = stateFilter.takeIf { it.isNotBlank() },
        requiredAmenities = buildSet {
            if (requireDiesel) add(TruckStopAmenity.DIESEL)
            if (requireShowers) add(TruckStopAmenity.SHOWERS)
            if (requireFood) add(TruckStopAmenity.FOOD)
            if (requireRepair) add(TruckStopAmenity.REPAIR)
        },
    )
    val result = remember(stops, filter) { TruckStopSearch.search(stops, filter) }
    val states = remember(stops) { stops.map { it.state }.distinct().sorted() }
    val vintages = remember(stops) {
        stops.mapNotNull { stop -> stop.datasetVintage.takeIf(String::isNotBlank) }.distinct()
    }

    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(WagonSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(WagonSpacing.md),
    ) {
        item {
            SectionHeader(
                "Directory preview", "Truck stops",
                "Labeled sample data only — fictional locations demonstrating offline search. " +
                    "The verified national dataset ships after source review. Amenities and " +
                    "parking are never guaranteed; unknown means unknown, not absent.",
            )
        }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search name, highway, or state") },
                leadingIcon = { Icon(Icons.Rounded.Search, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (states.isNotEmpty()) item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
            ) {
                states.forEach { state ->
                    FilterChip(
                        selected = stateFilter == state,
                        onClick = { stateFilter = if (stateFilter == state) "" else state },
                        label = { Text(state) },
                    )
                }
            }
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
            ) {
                FilterChip(requireDiesel, { requireDiesel = !requireDiesel }, { Text("Diesel") })
                FilterChip(requireShowers, { requireShowers = !requireShowers }, { Text("Showers") })
                FilterChip(requireFood, { requireFood = !requireFood }, { Text("Food") })
                FilterChip(requireRepair, { requireRepair = !requireRepair }, { Text("Repair") })
            }
        }
        if (result.hiddenUnknownCount > 0) item {
            StatePanel(
                "Hidden by amenity filters",
                result.hiddenUnknownCount.toString() + " matching stop(s) are hidden only " +
                    "because a selected amenity is unknown for them — unknown is not the same " +
                    "as unavailable. Clear amenity filters to see them.",
                Icons.Rounded.VisibilityOff, WagonColors.MarkerAmber,
            )
        }
        if (stops.isNotEmpty() && result.matches.isEmpty() && result.hiddenUnknownCount == 0) item {
            StatePanel(
                "No matches",
                "No stops match this search. Adjust the search text or filters.",
                Icons.Rounded.SearchOff, WagonColors.Steel500,
            )
        }
        items(result.matches, key = { it.id.value }) { stop -> TruckStopCard(stop) }
        items(vintages) { vintage ->
            StatePanel("Dataset provenance", vintage, Icons.Rounded.Info, WagonColors.DashboardBlue)
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable private fun TruckStopCard(stop: TruckStop) {
    WagonCard(Modifier.fillMaxWidth()) {
        Text(stop.name, style = MaterialTheme.typography.titleMedium)
        Text(
            stop.state + " • " + stop.highway,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            stop.truckParkingSpaces?.let { "Truck parking: $it spaces (not a live count)" }
                ?: "Truck parking: unknown",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
        ) {
            if (stop.isSample) WagonTag("Sample", WagonColors.MarkerAmber)
            val (statusLabel, statusColor) = verificationDisplay(stop.verificationStatus)
            WagonTag(statusLabel, statusColor)
            TruckStopAmenity.entries.forEach { amenity ->
                if (TruckStopSearch.amenityValue(stop, amenity) == true) {
                    WagonTag(amenityLabel(amenity), WagonColors.SignalGreen)
                }
            }
        }
        val unknown = TruckStopAmenity.entries.filter { TruckStopSearch.amenityValue(stop, it) == null }
        val notListed = TruckStopAmenity.entries.filter { TruckStopSearch.amenityValue(stop, it) == false }
        if (unknown.isNotEmpty()) {
            StopDetail(
                "Unknown",
                unknown.joinToString { amenityLabel(it) } +
                    " — not recorded by the source; may still exist.",
            )
        }
        if (notListed.isNotEmpty()) StopDetail("Not listed", notListed.joinToString { amenityLabel(it) })
        StopDetail("Source", stop.sourceCitation.ifBlank { "Not cited (sample)" })
    }
}

@Composable private fun StopDetail(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(WagonSpacing.xxs)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun amenityLabel(amenity: TruckStopAmenity): String = when (amenity) {
    TruckStopAmenity.DIESEL -> "Diesel"
    TruckStopAmenity.SHOWERS -> "Showers"
    TruckStopAmenity.FOOD -> "Food"
    TruckStopAmenity.REPAIR -> "Repair"
}

private fun verificationDisplay(status: VerificationStatus): Pair<String, Color> = when (status) {
    VerificationStatus.VERIFIED -> "Verified" to WagonColors.SignalGreen
    VerificationStatus.PARTIAL -> "Partial" to WagonColors.MarkerAmber
    VerificationStatus.UNVERIFIED -> "Unverified" to WagonColors.Steel500
}
