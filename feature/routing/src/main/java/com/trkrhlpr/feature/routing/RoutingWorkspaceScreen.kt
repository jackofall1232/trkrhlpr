package com.trkrhlpr.feature.routing

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trkrhlpr.core.model.*
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun RoutingWorkspaceScreen(repository: VehicleProfileRepository, modifier: Modifier = Modifier) {
    val profile by repository.profile.collectAsStateWithLifecycle(initialValue = null)
    var editing by rememberSaveable { mutableStateOf(false) }
    var showMap by rememberSaveable { mutableStateOf(false) }

    if (showMap) {
        Column(modifier.fillMaxSize()) {
            TextButton(onClick = { showMap = false }, modifier = Modifier.padding(horizontal = 8.dp)) {
                Text("Back to vehicle profile")
            }
            RoutingMapScreen(Modifier.weight(1f))
        }
    } else if (profile == null || editing) {
        VehicleProfileEditor(
            existing = profile,
            repository = repository,
            onSaved = { editing = false },
            modifier = modifier,
        )
    } else {
        VehicleProfileSummary(
            profile = requireNotNull(profile),
            onEdit = { editing = true },
            onOpenMap = { showMap = true },
            modifier = modifier,
        )
    }
}

@Composable
private fun VehicleProfileEditor(
    existing: VehicleProfile?,
    repository: VehicleProfileRepository,
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
                            repository.save(valid)
                            onSaved()
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
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Confirmed vehicle profile", style = MaterialTheme.typography.headlineMedium)
        Text(profile.vehicleType.displayName, style = MaterialTheme.typography.titleLarge)
        Text("Height ${profile.heightMeters.toFeetText()} ft · Width ${profile.widthMeters.toFeetText()} ft")
        Text("Length ${profile.lengthMeters.toFeetText()} ft · ${profile.axleCount} axles")
        Text("Gross ${profile.grossWeightTonnes.toPoundsText()} lb · Axle load ${profile.axleLoadTonnes.toPoundsText()} lb")
        Text(if (profile.hazmat) "Hazmat: yes" else "Hazmat: no")
        Text("Reconfirm after any equipment or load change.", color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) { Text("Edit and reconfirm") }
        Button(onClick = onOpenMap, modifier = Modifier.fillMaxWidth()) { Text("Open map preview") }
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

private fun Double.toFeetText() = String.format(Locale.US, "%.2f", VehicleUnitConversions.metersToFeet(this))
private fun Double.toPoundsText() = String.format(Locale.US, "%.0f", VehicleUnitConversions.metricTonnesToPounds(this))
