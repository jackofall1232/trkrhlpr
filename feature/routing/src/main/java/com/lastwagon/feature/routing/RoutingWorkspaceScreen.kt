package com.lastwagon.feature.routing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwagon.core.model.*
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RoutingWorkspaceScreen(
    repository: VehicleProfileRepository,
    routingProvider: RoutingProvider,
    routeRepository: RouteRepository,
    modifier: Modifier = Modifier,
    styleProvider: MapStyleProvider = OpenFreeMapLibertyStyleProvider,
    corridorManager: OfflineCorridorManager? = null,
    networkMonitor: NetworkMonitor? = null,
) {
    val context = LocalContext.current
    val corridor = corridorManager ?: remember { OfflineCorridorManager(context) }
    val monitor = networkMonitor ?: remember { NetworkMonitor(context) }
    val profile by repository.profile.collectAsStateWithLifecycle(initialValue = null)
    val savedRoute by routeRepository.lastRoute.collectAsStateWithLifecycle(initialValue = null)
    val corridorState by corridor.state.collectAsStateWithLifecycle()
    val online by monitor.online.collectAsStateWithLifecycle()
    var editing by rememberSaveable { mutableStateOf(false) }
    var planning by rememberSaveable { mutableStateOf(false) }
    var reviewing by rememberSaveable { mutableStateOf(false) }
    var showMap by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // A corridor is only ever kept for the exact saved route; anything else is deleted.
    // Collect the repository flow directly: its first emission is the real persisted
    // state, so a stored corridor is never discarded on the pre-load null placeholder.
    LaunchedEffect(routeRepository, corridor) {
        routeRepository.lastRoute
            .distinctUntilChangedBy { it?.provenance?.requestId to it?.review }
            .collect { corridor.refresh(it) }
    }

    if (showMap && canOpenRouteMap(savedRoute)) {
        Column(modifier.fillMaxSize()) {
            TextButton(onClick = { showMap = false }, modifier = Modifier.padding(horizontal = 8.dp)) {
                Text("Back to vehicle profile")
            }
            RoutingMapScreen(
                Modifier.weight(1f),
                styleProvider = styleProvider,
                route = savedRoute,
                online = online,
                corridorState = corridorState,
            )
        }
    } else if (reviewing && savedRoute != null && profile != null) {
        RouteReviewScreen(
            route = requireNotNull(savedRoute),
            currentProfile = requireNotNull(profile),
            routeRepository = routeRepository,
            onBack = { reviewing = false },
            onReviewed = { reviewing = false; showMap = true },
            modifier = modifier,
        )
    } else if (planning && profile != null) {
        RoutePlanner(
            profile = requireNotNull(profile),
            routingProvider = routingProvider,
            routeRepository = routeRepository,
            online = online,
            onBack = { planning = false },
            onRouteSaved = { planning = false; reviewing = true },
            modifier = modifier,
        )
    } else if (profile == null || editing) {
        VehicleProfileEditor(
            existing = profile,
            repository = repository,
            routeRepository = routeRepository,
            onSaved = { editing = false },
            modifier = modifier,
        )
    } else {
        VehicleProfileSummary(
            profile = requireNotNull(profile),
            onEdit = { editing = true },
            savedRoute = savedRoute,
            onPlanRoute = { planning = true },
            onReviewRoute = { reviewing = true },
            onOpenMap = { if (canOpenRouteMap(savedRoute)) showMap = true },
            onDeleteRoute = {
                corridor.delete()
                scope.launch { routeRepository.clear() }
            },
            corridorState = corridorState,
            mapStyle = styleProvider.style(),
            online = online,
            onDownloadCorridor = { detail ->
                savedRoute?.let { corridor.download(it, styleProvider.style(), detail) }
            },
            onCancelCorridor = { corridor.cancel() },
            onDeleteCorridor = { corridor.delete() },
            modifier = modifier,
        )
    }
}

@Composable
private fun VehicleProfileEditor(
    existing: VehicleProfile?,
    repository: VehicleProfileRepository,
    routeRepository: RouteRepository,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var vehicleType by rememberSaveable { mutableStateOf(existing?.vehicleType) }
    var typeMenuOpen by remember { mutableStateOf(false) }
    var heightFeet by rememberSaveable { mutableStateOf(existing?.heightMeters?.toFeetText().orEmpty()) }
    var widthFeet by rememberSaveable { mutableStateOf(existing?.widthMeters?.toFeetText().orEmpty()) }
    var lengthFeet by rememberSaveable { mutableStateOf(existing?.lengthMeters?.toFeetText().orEmpty()) }
    var grossPounds by rememberSaveable { mutableStateOf(existing?.grossWeightTonnes?.toPoundsText().orEmpty()) }
    var axlePounds by rememberSaveable { mutableStateOf(existing?.axleLoadTonnes?.toPoundsText().orEmpty()) }
    var axleCount by rememberSaveable { mutableStateOf(existing?.axleCount?.toString().orEmpty()) }
    var hazmat by rememberSaveable { mutableStateOf(existing?.hazmat ?: false) }
    var avoidTolls by rememberSaveable { mutableStateOf(existing?.avoidTolls ?: false) }
    var avoidFerries by rememberSaveable { mutableStateOf(existing?.avoidFerries ?: false) }
    var avoidUnpaved by rememberSaveable { mutableStateOf(existing?.avoidUnpavedRoads ?: false) }
    var confirmed by rememberSaveable { mutableStateOf(false) }
    var errors by remember { mutableStateOf(emptyList<String>()) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Vehicle profile", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Enter the current vehicle and load. Values are validation inputs, not proof that a route is legal or clear.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            Box {
                OutlinedButton(onClick = { typeMenuOpen = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(vehicleType?.displayName ?: "Select vehicle type", Modifier.weight(1f))
                    Icon(Icons.Rounded.ArrowDropDown, null)
                }
                DropdownMenu(expanded = typeMenuOpen, onDismissRequest = { typeMenuOpen = false }) {
                    CommercialVehicleType.entries.forEach { type ->
                        DropdownMenuItem(text = { Text(type.displayName) }, onClick = {
                            vehicleType = type
                            typeMenuOpen = false
                        })
                    }
                }
            }
        }
        item { DecimalField(heightFeet, { heightFeet = it }, "Overall height (decimal feet)", "Example: 13.5 = 13 ft 6 in") }
        item { DecimalField(widthFeet, { widthFeet = it }, "Overall width (decimal feet)") }
        item { DecimalField(lengthFeet, { lengthFeet = it }, "Overall length (feet)") }
        item { DecimalField(grossPounds, { grossPounds = it }, "Gross vehicle weight (lb)") }
        item { DecimalField(axlePounds, { axlePounds = it }, "Maximum axle load (lb)") }
        item { DecimalField(axleCount, { axleCount = it }, "Axle count", integer = true) }
        item { ToggleRow("Carrying hazardous materials", hazmat) { hazmat = it } }
        item { Text("Route preferences", style = MaterialTheme.typography.titleMedium) }
        item { ToggleRow("Avoid tolls", avoidTolls) { avoidTolls = it } }
        item { ToggleRow("Avoid ferries", avoidFerries) { avoidFerries = it } }
        item { ToggleRow("Avoid unpaved roads", avoidUnpaved) { avoidUnpaved = it } }
        item {
            Row(verticalAlignment = Alignment.Top) {
                Checkbox(checked = confirmed, onCheckedChange = { confirmed = it })
                Text(
                    "I checked these values against the current vehicle, trailer, and load. I will update them when equipment or load changes.",
                    Modifier.padding(top = 12.dp),
                )
            }
        }
        if (errors.isNotEmpty()) {
            items(errors) { error -> Text(error, color = MaterialTheme.colorScheme.error) }
        }
        item {
            Button(
                onClick = {
                    val candidate = buildProfile(
                        vehicleType, heightFeet, widthFeet, lengthFeet, grossPounds,
                        axlePounds, axleCount, hazmat, avoidTolls, avoidFerries,
                        avoidUnpaved, confirmed,
                    )
                    errors = candidate.second
                    candidate.first?.let { valid ->
                        scope.launch {
                            try {
                                routeRepository.clear()
                                repository.save(valid)
                                onSaved()
                            } catch (_: Exception) {
                                errors = listOf("The profile could not be saved locally.")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) { Text("Confirm and save profile") }
        }
    }
}

@Composable
private fun VehicleProfileSummary(
    profile: VehicleProfile,
    onEdit: () -> Unit,
    savedRoute: CalculatedRoute?,
    onPlanRoute: () -> Unit,
    onReviewRoute: () -> Unit,
    onOpenMap: () -> Unit,
    onDeleteRoute: () -> Unit,
    corridorState: CorridorState,
    mapStyle: MapStyleDescriptor,
    online: Boolean,
    onDownloadCorridor: (CorridorDetail) -> Unit,
    onCancelCorridor: () -> Unit,
    onDeleteCorridor: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Confirmed vehicle profile", style = MaterialTheme.typography.headlineMedium)
            Text(profile.vehicleType.displayName, style = MaterialTheme.typography.titleLarge)
            Text("Height ${profile.heightMeters.toFeetText()} ft · Width ${profile.widthMeters.toFeetText()} ft")
            Text("Length ${profile.lengthMeters.toFeetText()} ft · ${profile.axleCount} axles")
            Text("Gross ${profile.grossWeightTonnes.toPoundsText()} lb · Axle load ${profile.axleLoadTonnes.toPoundsText()} lb")
            Text(if (profile.hazmat) "Hazmat: yes" else "Hazmat: no")
            Text("Reconfirm after any equipment or load change.", color = MaterialTheme.colorScheme.error)
        }
        if (!online) item {
            Text("OFFLINE — saved data only. New routes cannot be calculated.",
                color = MaterialTheme.colorScheme.error)
        }
        item {
            OutlinedButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) { Text("Edit and reconfirm") }
        }
        item {
            Button(onClick = onPlanRoute, modifier = Modifier.fillMaxWidth()) { Text("Calculate route preview") }
        }
        if (savedRoute != null) {
            item {
                if (savedRoute.hasCurrentDriverReview) {
                    Text("Route state: DRIVER REVIEWED", color = MaterialTheme.colorScheme.tertiary)
                    OutlinedButton(onClick = onOpenMap, modifier = Modifier.fillMaxWidth()) {
                        Text("Open reviewed route")
                    }
                } else {
                    Text("Route state: UNVERIFIED", color = MaterialTheme.colorScheme.error)
                    OutlinedButton(onClick = onReviewRoute, modifier = Modifier.fillMaxWidth()) {
                        Text("Review saved route")
                    }
                }
                if (savedRoute.warnings.isNotEmpty())
                    Text("Route state: DATA WARNING", color = MaterialTheme.colorScheme.error)
                if (!online || savedRoute.isStale())
                    Text("Route state: OFFLINE / STALE", color = MaterialTheme.colorScheme.error)
            }
            if (savedRoute.hasCurrentDriverReview) {
                item {
                    OfflineCorridorCard(
                        route = savedRoute,
                        corridorState = corridorState,
                        mapStyle = mapStyle,
                        online = online,
                        onDownload = onDownloadCorridor,
                        onCancel = onCancelCorridor,
                        onDelete = onDeleteCorridor,
                    )
                }
            }
            item {
                TextButton(onClick = onDeleteRoute, modifier = Modifier.fillMaxWidth()) {
                    Text("Delete saved route")
                }
            }
        }
    }
}

@Composable
private fun OfflineCorridorCard(
    route: CalculatedRoute,
    corridorState: CorridorState,
    mapStyle: MapStyleDescriptor,
    online: Boolean,
    onDownload: (CorridorDetail) -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Offline route corridor", style = MaterialTheme.typography.titleLarge)
            Text(
                "Saves map data along this reviewed route for viewing without a connection. " +
                    "Offline viewing never updates restrictions and never reroutes.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            when (corridorState) {
                is CorridorState.None, is CorridorState.Failed -> {
                    if (corridorState is CorridorState.Failed) {
                        Text(corridorState.message, color = MaterialTheme.colorScheme.error)
                    }
                    val refusal = OfflineCorridor.downloadRefusalReason(route, mapStyle)
                    if (refusal != null) {
                        Text(refusal, color = MaterialTheme.colorScheme.error)
                    } else if (!online) {
                        Text("Reconnect to download the corridor for offline use.",
                            color = MaterialTheme.colorScheme.error)
                    } else {
                        var selected by rememberSaveable { mutableStateOf(CorridorDetail.STANDARD) }
                        var menuOpen by remember { mutableStateOf(false) }
                        val estimate = remember(route.provenance.requestId, selected) {
                            OfflineCorridor.estimateTileCount(route.geometry, selected)
                        }
                        Box {
                            OutlinedButton(onClick = { menuOpen = true }, modifier = Modifier.fillMaxWidth()) {
                                Text(selected.displayName, Modifier.weight(1f))
                                Icon(Icons.Rounded.ArrowDropDown, null)
                            }
                            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                                CorridorDetail.entries.forEach { detail ->
                                    DropdownMenuItem(text = { Text(detail.displayName) }, onClick = {
                                        selected = detail
                                        menuOpen = false
                                    })
                                }
                            }
                        }
                        Text(
                            "About $estimate map tiles (limit ${OfflineCorridor.MAX_TILES}). " +
                                "Exact size is shown while downloading.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Button(
                            onClick = { onDownload(selected) },
                            enabled = estimate <= OfflineCorridor.MAX_TILES,
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Download offline corridor") }
                        if (estimate > OfflineCorridor.MAX_TILES) {
                            Text("Too large at this detail level — choose a lower one.",
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                is CorridorState.Preparing -> {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                    Text("Preparing corridor download…")
                    TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
                }
                is CorridorState.Downloading -> {
                    if (corridorState.requiredCountIsPrecise && corridorState.requiredResources > 0) {
                        LinearProgressIndicator(
                            progress = {
                                (corridorState.completedResources.toFloat() /
                                    corridorState.requiredResources).coerceIn(0f, 1f)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text("Downloading ${corridorState.completedResources} of " +
                            "${corridorState.requiredResources} resources · " +
                            corridorState.completedBytes.toMegabytesText())
                    } else {
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                        Text("Downloading… ${corridorState.completedBytes.toMegabytesText()} so far")
                    }
                    TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancel and delete download")
                    }
                }
                is CorridorState.Ready -> {
                    val expired = OfflineCorridor.isExpired(
                        corridorState.metadata, System.currentTimeMillis(),
                    )
                    Text(
                        "Corridor saved · ${corridorState.completedBytes.toMegabytesText()} · " +
                            "${corridorState.completedTiles} tiles",
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text("Downloaded ${corridorState.metadata.createdAtEpochMillis.toDateText()} · " +
                        "expires ${corridorState.metadata.expiresAtEpochMillis.toDateText()}")
                    if (expired) {
                        Text(
                            "CORRIDOR EXPIRED — map and restriction data may be outdated. " +
                                "Delete it and download again while connected.",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                        Text("Delete offline corridor")
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutePlanner(
    profile: VehicleProfile,
    routingProvider: RoutingProvider,
    routeRepository: RouteRepository,
    online: Boolean,
    onBack: () -> Unit,
    onRouteSaved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var originLatitude by rememberSaveable { mutableStateOf("") }
    var originLongitude by rememberSaveable { mutableStateOf("") }
    var destinationLatitude by rememberSaveable { mutableStateOf("") }
    var destinationLongitude by rememberSaveable { mutableStateOf("") }
    var calculating by remember { mutableStateOf(false) }
    var failure by remember { mutableStateOf<RouteFailure?>(null) }
    var result by remember { mutableStateOf<CalculatedRoute?>(null) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Online route preview", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Phase 3 accepts coordinates only. Results are unreviewed decision support based on available data—not proof of legality, clearance, or safety.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!online) item {
            Text(
                "OFFLINE — new routes cannot be calculated and this app never reroutes offline. Reconnect to plan a route.",
                color = MaterialTheme.colorScheme.error,
            )
        }
        item { DecimalField(originLatitude, { originLatitude = it }, "Origin latitude") }
        item { DecimalField(originLongitude, { originLongitude = it }, "Origin longitude") }
        item { DecimalField(destinationLatitude, { destinationLatitude = it }, "Destination latitude") }
        item { DecimalField(destinationLongitude, { destinationLongitude = it }, "Destination longitude") }
        failure?.let { error -> item {
            Text(error.message, color = MaterialTheme.colorScheme.error)
            error.httpStatus?.let { Text("Provider HTTP status: $it") }
        } }
        result?.let { route ->
            item {
                Text("Unreviewed route calculated", style = MaterialTheme.typography.titleLarge)
                Text("${route.distanceMeters.toMilesText()} mi · ${route.durationSeconds.toDurationText()}")
                Text("Provider: ${route.provenance.providerId} · Request ${route.provenance.requestId}")
                Text("Response SHA-256: ${route.provenance.responseSha256}",
                    style = MaterialTheme.typography.labelSmall)
            }
            items(route.warnings) { warning -> Text("Warning: $warning", color = MaterialTheme.colorScheme.error) }
            item {
                Button(onClick = onRouteSaved, modifier = Modifier.fillMaxWidth()) {
                    Text("Review route before map access")
                }
            }
        }
        item {
            Button(
                enabled = !calculating && online,
                onClick = {
                    val origin = GeoPoint(originLatitude.toDoubleOrNull() ?: Double.NaN,
                        originLongitude.toDoubleOrNull() ?: Double.NaN)
                    val destination = GeoPoint(destinationLatitude.toDoubleOrNull() ?: Double.NaN,
                        destinationLongitude.toDoubleOrNull() ?: Double.NaN)
                    calculating = true
                    failure = null
                    result = null
                    scope.launch {
                        try {
                            when (val calculated = routingProvider.calculate(RouteRequest(origin, destination, profile))) {
                                is RouteCalculationResult.Failure -> failure = calculated.error
                                is RouteCalculationResult.Success -> {
                                    routeRepository.save(calculated.route)
                                    result = calculated.route
                                }
                            }
                        } catch (_: Exception) {
                            failure = RouteFailure(RouteFailureKind.STORAGE,
                                "The calculated route could not be saved locally.")
                        } finally {
                            calculating = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (calculating) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Calculate with OpenRouteService")
            }
        }
        item { TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to vehicle profile") } }
    }
}

@Composable
private fun RouteReviewScreen(
    route: CalculatedRoute,
    currentProfile: VehicleProfile,
    routeRepository: RouteRepository,
    onBack: () -> Unit,
    onReviewed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var checkedOfficialSources by rememberSaveable { mutableStateOf(false) }
    var checkedVehicleAndLoad by rememberSaveable { mutableStateOf(false) }
    var acceptsRealWorldPriority by rememberSaveable { mutableStateOf(false) }
    var recording by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val profileMatches = route.request.vehicleProfile.confirmedAtEpochMillis ==
        currentProfile.confirmedAtEpochMillis
    val canRecord = profileMatches && checkedOfficialSources && checkedVehicleAndLoad &&
        acceptsRealWorldPriority && !recording

    LazyColumn(
        modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Mandatory route review", style = MaterialTheme.typography.headlineMedium)
            Text("Route state: UNVERIFIED", color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium)
            if (route.warnings.isNotEmpty()) Text("Route state: DATA WARNING",
                color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleMedium)
            if (route.isStale()) Text("Route state: OFFLINE / STALE",
                color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleMedium)
        }
        item {
            Text("Route overview", style = MaterialTheme.typography.titleLarge)
            Text("Origin: ${route.request.origin.latitude}, ${route.request.origin.longitude}")
            Text("Destination: ${route.request.destination.latitude}, ${route.request.destination.longitude}")
            Text("${route.distanceMeters.toMilesText()} mi · ${route.durationSeconds.toDurationText()}")
        }
        item {
            val vehicle = route.request.vehicleProfile
            Text("Vehicle used for calculation", style = MaterialTheme.typography.titleLarge)
            Text(vehicle.vehicleType.displayName)
            Text("Height ${vehicle.heightMeters.toFeetText()} ft · Width ${vehicle.widthMeters.toFeetText()} ft")
            Text("Length ${vehicle.lengthMeters.toFeetText()} ft · ${vehicle.axleCount} axles")
            Text("Gross ${vehicle.grossWeightTonnes.toPoundsText()} lb · Axle ${vehicle.axleLoadTonnes.toPoundsText()} lb")
            Text(if (vehicle.hazmat) "Hazmat: yes" else "Hazmat: no")
        }
        if (!profileMatches) item {
            Text("The current vehicle profile no longer matches this route. Recalculate it.",
                color = MaterialTheme.colorScheme.error)
        }
        item { Text("Warnings and data gaps", style = MaterialTheme.typography.titleLarge) }
        items(route.warnings) { warning -> Text("• $warning", color = MaterialTheme.colorScheme.error) }
        item {
            Text("Restriction segments reported: ${route.roadAccessRestrictionSegments}")
            Text("Missing restriction data is unknown, never proof of clearance.",
                color = MaterialTheme.colorScheme.error)
        }
        item { Text("Route steps", style = MaterialTheme.typography.titleLarge) }
        items(route.steps) { step ->
            Text("${step.instruction} · ${step.distanceMeters.toMilesText()} mi")
        }
        item {
            Text("Provider provenance", style = MaterialTheme.typography.titleLarge)
            Text("${route.provenance.providerId} · ${route.provenance.routingProfile}")
            Text("Request: ${route.provenance.requestId}")
            Text("Response SHA-256: ${route.provenance.responseSha256}",
                style = MaterialTheme.typography.labelSmall)
            route.provenance.graphDate?.let { Text("Routing graph date: $it") }
            route.provenance.responseAttribution?.let { Text(it) }
        }
        item {
            ReviewCheckbox(
                checkedOfficialSources,
                "I compared this route with applicable official restrictions, permits, and dispatch instructions.",
            ) { checkedOfficialSources = it }
        }
        item {
            ReviewCheckbox(
                checkedVehicleAndLoad,
                "I verified the vehicle and load shown above are current.",
            ) { checkedVehicleAndLoad = it }
        }
        item {
            ReviewCheckbox(
                acceptsRealWorldPriority,
                "I understand road signs, law enforcement, permits, closures, weather, and real-world conditions override this app.",
            ) { acceptsRealWorldPriority = it }
        }
        error?.let { message -> item { Text(message, color = MaterialTheme.colorScheme.error) } }
        item {
            Button(
                enabled = canRecord,
                onClick = {
                    recording = true
                    error = null
                    scope.launch {
                        try {
                            val recorded = routeRepository.recordReview(RouteReview(
                                route.provenance.requestId,
                                route.request.vehicleProfile.confirmedAtEpochMillis,
                                System.currentTimeMillis(),
                            ))
                            if (recorded) onReviewed()
                            else error = "The route or vehicle profile changed. Reopen the review."
                        } catch (_: Exception) {
                            error = "The review acknowledgment could not be saved locally."
                        } finally {
                            recording = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Record driver review") }
        }
        item { TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back without reviewing") } }
    }
}

@Composable
private fun ReviewCheckbox(checked: Boolean, label: String, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun DecimalField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: String? = null,
    integer: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        supportingText = if (supportingText == null) null else {{ Text(supportingText) }},
        keyboardOptions = KeyboardOptions(keyboardType = if (integer) KeyboardType.Number else KeyboardType.Decimal),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

private fun buildProfile(
    type: CommercialVehicleType?, heightFeet: String, widthFeet: String, lengthFeet: String,
    grossPounds: String, axlePounds: String, axleCount: String, hazmat: Boolean,
    avoidTolls: Boolean, avoidFerries: Boolean, avoidUnpaved: Boolean, confirmed: Boolean,
): Pair<VehicleProfile?, List<String>> {
    val missing = mutableListOf<String>()
    if (type == null) missing += "Select a vehicle type."
    val height = heightFeet.toDoubleOrNull().also { if (it == null) missing += "Enter a numeric overall height." }
    val width = widthFeet.toDoubleOrNull().also { if (it == null) missing += "Enter a numeric overall width." }
    val length = lengthFeet.toDoubleOrNull().also { if (it == null) missing += "Enter a numeric overall length." }
    val gross = grossPounds.toDoubleOrNull().also { if (it == null) missing += "Enter a numeric gross weight." }
    val axle = axlePounds.toDoubleOrNull().also { if (it == null) missing += "Enter a numeric axle load." }
    val axles = axleCount.toIntOrNull().also { if (it == null) missing += "Enter a whole-number axle count." }
    if (!confirmed) missing += "Confirm that the profile matches the current vehicle and load."
    if (missing.isNotEmpty()) return null to missing

    val profile = VehicleProfile(
        requireNotNull(type), VehicleUnitConversions.feetToMeters(requireNotNull(height)),
        VehicleUnitConversions.feetToMeters(requireNotNull(width)),
        VehicleUnitConversions.feetToMeters(requireNotNull(length)),
        VehicleUnitConversions.poundsToMetricTonnes(requireNotNull(gross)),
        VehicleUnitConversions.poundsToMetricTonnes(requireNotNull(axle)), requireNotNull(axles),
        hazmat, avoidTolls, avoidFerries, avoidUnpaved, System.currentTimeMillis(),
    )
    val validationErrors = VehicleProfileValidator.validate(profile).map { it.message }
    return profile.takeIf { validationErrors.isEmpty() } to validationErrors
}

private val CommercialVehicleType.displayName get() = when (this) {
    CommercialVehicleType.TRACTOR_TRAILER -> "Tractor-trailer"
    CommercialVehicleType.STRAIGHT_TRUCK -> "Straight truck"
    CommercialVehicleType.BUS -> "Bus"
    CommercialVehicleType.DELIVERY -> "Delivery vehicle"
    CommercialVehicleType.OTHER -> "Other commercial vehicle"
}

private fun Long.toMegabytesText() = String.format(Locale.US, "%.1f MB", this / (1024.0 * 1024.0))
private fun Long.toDateText(): String = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(this))
private fun Double.toFeetText() = String.format(Locale.US, "%.2f", VehicleUnitConversions.metersToFeet(this))
private fun Double.toPoundsText() = String.format(Locale.US, "%.0f", VehicleUnitConversions.metricTonnesToPounds(this))
private fun Double.toMilesText() = String.format(Locale.US, "%.1f", this / 1609.344)
private fun Double.toDurationText(): String {
    val totalMinutes = (this / 60.0).toInt()
    return "${totalMinutes / 60} hr ${totalMinutes % 60} min"
}
private fun CalculatedRoute.isStale(now: Long = System.currentTimeMillis()) =
    now - provenance.receivedAtEpochMillis > 24 * 60 * 60 * 1000L
internal fun canOpenRouteMap(route: CalculatedRoute?) = route?.hasCurrentDriverReview == true
