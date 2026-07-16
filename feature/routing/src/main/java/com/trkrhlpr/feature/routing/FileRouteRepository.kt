package com.trkrhlpr.feature.routing

import android.content.Context
import com.trkrhlpr.core.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.io.File
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class FileRouteRepository(context: Context) : RouteRepository {
    private val file = File(context.filesDir, "last-route-v1.json")
    private val state = MutableStateFlow(readRouteFile(file))
    override val lastRoute = state

    override suspend fun save(route: CalculatedRoute) = withContext(Dispatchers.IO) {
        val temporary = File(file.parentFile, "${file.name}.tmp")
        temporary.writeText(encodeRoute(route))
        try {
            Files.move(temporary.toPath(), file.toPath(),
                StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
        } catch (_: AtomicMoveNotSupportedException) {
            Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        state.value = route
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        if (file.exists()) check(file.delete()) { "Could not delete saved route" }
        state.value = null
    }
}

internal fun readRouteFile(file: File): CalculatedRoute? =
    runCatching { if (file.exists()) decodeRoute(file.readText()) else null }.getOrNull()

internal fun encodeRoute(route: CalculatedRoute): String = buildJsonObject {
    put("schema_version", 1)
    putJsonObject("request") {
        putPoint("origin", route.request.origin)
        putPoint("destination", route.request.destination)
        putJsonObject("vehicle") { putVehicle(route.request.vehicleProfile) }
    }
    putJsonArray("geometry") { route.geometry.forEach { add(buildPoint(it)) } }
    put("distance_meters", route.distanceMeters)
    put("duration_seconds", route.durationSeconds)
    putJsonArray("steps") { route.steps.forEach { step -> addJsonObject {
        put("instruction", step.instruction)
        put("distance_meters", step.distanceMeters)
        put("duration_seconds", step.durationSeconds)
    } } }
    putJsonArray("warnings") { route.warnings.forEach(::add) }
    put("restriction_segments", route.roadAccessRestrictionSegments)
    putJsonObject("provenance") {
        val p = route.provenance
        put("request_id", p.requestId); put("provider_id", p.providerId)
        put("endpoint", p.endpoint); put("routing_profile", p.routingProfile)
        put("requested_at", p.requestedAtEpochMillis); put("received_at", p.receivedAtEpochMillis)
        put("request_payload", p.requestPayload); put("response_sha256", p.responseSha256)
        putNullable("attribution", p.responseAttribution); putNullable("service", p.responseService)
        p.responseTimestampEpochMillis?.let { put("response_timestamp", it) }
        putNullable("engine_version", p.engineVersion); putNullable("engine_build_date", p.engineBuildDate)
        putNullable("graph_date", p.graphDate)
    }
}.toString()

internal fun decodeRoute(value: String): CalculatedRoute? = runCatching {
    val root = Json.parseToJsonElement(value).jsonObject
    require(root["schema_version"]?.jsonPrimitive?.int == 1)
    val request = root.getValue("request").jsonObject
    val vehicle = request.getValue("vehicle").jsonObject.toVehicle()
    val routeRequest = RouteRequest(
        request.getValue("origin").jsonObject.toPoint(),
        request.getValue("destination").jsonObject.toPoint(), vehicle,
    )
    val provenance = root.getValue("provenance").jsonObject
    CalculatedRoute(
        routeRequest,
        root.getValue("geometry").jsonArray.map { it.jsonObject.toPoint() },
        root.getValue("distance_meters").jsonPrimitive.double,
        root.getValue("duration_seconds").jsonPrimitive.double,
        root.getValue("steps").jsonArray.map { stepValue -> stepValue.jsonObject.let { step ->
            RouteStep(step.getValue("instruction").jsonPrimitive.content,
                step.getValue("distance_meters").jsonPrimitive.double,
                step.getValue("duration_seconds").jsonPrimitive.double)
        } },
        root.getValue("warnings").jsonArray.map { it.jsonPrimitive.content },
        root.getValue("restriction_segments").jsonPrimitive.int,
        RouteProvenance(
            provenance.string("request_id"), provenance.string("provider_id"),
            provenance.string("endpoint"), provenance.string("routing_profile"),
            provenance.long("requested_at"), provenance.long("received_at"),
            provenance.string("request_payload"), provenance.string("response_sha256"),
            provenance.optionalString("attribution"), provenance.optionalString("service"),
            provenance["response_timestamp"]?.jsonPrimitive?.longOrNull,
            provenance.optionalString("engine_version"), provenance.optionalString("engine_build_date"),
            provenance.optionalString("graph_date"),
        ),
    ).takeIf { route ->
        route.geometry.size >= 2 && route.geometry.all { it.isValid } &&
            route.distanceMeters.isFinite() && route.distanceMeters >= 0 &&
            VehicleProfileValidator.validate(route.request.vehicleProfile).isEmpty()
    }
}.getOrNull()

private fun JsonObjectBuilder.putPoint(name: String, point: GeoPoint) = putJsonObject(name) {
    put("latitude", point.latitude); put("longitude", point.longitude)
}
private fun buildPoint(point: GeoPoint) = buildJsonObject {
    put("latitude", point.latitude); put("longitude", point.longitude)
}
private fun JsonObject.toPoint() = GeoPoint(double("latitude"), double("longitude"))
private fun JsonObjectBuilder.putVehicle(v: VehicleProfile) {
    put("type", v.vehicleType.name); put("height_m", v.heightMeters); put("width_m", v.widthMeters)
    put("length_m", v.lengthMeters); put("gross_t", v.grossWeightTonnes)
    put("axle_t", v.axleLoadTonnes); put("axles", v.axleCount); put("hazmat", v.hazmat)
    put("avoid_tolls", v.avoidTolls); put("avoid_ferries", v.avoidFerries)
    put("avoid_unpaved", v.avoidUnpavedRoads); put("confirmed_at", v.confirmedAtEpochMillis)
}
private fun JsonObject.toVehicle() = VehicleProfile(
    CommercialVehicleType.valueOf(string("type")), double("height_m"), double("width_m"),
    double("length_m"), double("gross_t"), double("axle_t"), int("axles"),
    boolean("hazmat"), boolean("avoid_tolls"), boolean("avoid_ferries"),
    boolean("avoid_unpaved"), long("confirmed_at"),
)
private fun JsonObject.string(key: String) = getValue(key).jsonPrimitive.content
private fun JsonObject.optionalString(key: String) = get(key)?.jsonPrimitive?.contentOrNull
private fun JsonObject.double(key: String) = getValue(key).jsonPrimitive.double
private fun JsonObject.int(key: String) = getValue(key).jsonPrimitive.int
private fun JsonObject.long(key: String) = getValue(key).jsonPrimitive.long
private fun JsonObject.boolean(key: String) = getValue(key).jsonPrimitive.boolean
private fun JsonObjectBuilder.putNullable(key: String, value: String?) {
    if (value == null) put(key, JsonNull) else put(key, value)
}
