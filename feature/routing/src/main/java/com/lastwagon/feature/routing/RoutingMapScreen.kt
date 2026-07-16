package com.lastwagon.feature.routing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory.lineColor
import org.maplibre.android.style.layers.PropertyFactory.lineOpacity
import org.maplibre.android.style.layers.PropertyFactory.lineWidth
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
import com.lastwagon.core.model.CalculatedRoute
import com.lastwagon.core.model.hasCurrentDriverReview

private enum class MapLoadState { LOADING, READY }

@Composable
fun RoutingMapScreen(
    modifier: Modifier = Modifier,
    styleProvider: MapStyleProvider = MapLibreDemoStyleProvider,
    route: CalculatedRoute? = null,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val descriptor = remember(styleProvider) { styleProvider.style() }
    val mapView = remember {
        MapLibre.getInstance(context.applicationContext)
        MapView(context).apply { onCreate(null) }
    }
    var map by remember { mutableStateOf<MapLibreMap?>(null) }
    var style by remember { mutableStateOf<Style?>(null) }
    var loadState by remember { mutableStateOf(MapLoadState.LOADING) }
    var locationGranted by remember {
        mutableStateOf(context.hasCoarseLocationPermission())
    }
    var locationDenied by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        locationGranted = granted
        locationDenied = !granted
    }

    DisposableEffect(mapView, lifecycleOwner) {
        var started = false
        var resumed = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView.onStart()
                    started = true
                }
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    resumed = true
                    val hasPermission = context.hasCoarseLocationPermission()
                    locationGranted = hasPermission
                    if (hasPermission) locationDenied = false
                }
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                    resumed = false
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    started = false
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // Composition can end while the host Activity remains resumed (for example,
            // when navigating back), so finish any active MapView states before destroy.
            if (resumed) mapView.onPause()
            if (started) mapView.onStop()
            mapView.onDestroy()
        }
    }

    LaunchedEffect(mapView, descriptor.styleUri, route?.provenance?.requestId) {
        mapView.getMapAsync { readyMap ->
            map = readyMap
            readyMap.cameraPosition = CameraPosition.Builder()
                .target(LatLng(39.5, -98.35))
                .zoom(3.25)
                .build()
            readyMap.setStyle(descriptor.styleUri) { readyStyle ->
                route?.let { calculated ->
                    val points = calculated.geometry.map { Point.fromLngLat(it.longitude, it.latitude) }
                    readyStyle.addSource(GeoJsonSource("calculated-route", LineString.fromLngLats(points)))
                    readyStyle.addLayer(LineLayer("calculated-route-line", "calculated-route").withProperties(
                        lineColor("#FFB020"), lineWidth(6f), lineOpacity(0.9f),
                    ))
                    mapView.post {
                        val bounds = LatLngBounds.Builder().includes(
                            calculated.geometry.map { LatLng(it.latitude, it.longitude) },
                        ).build()
                        readyMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                    }
                }
                style = readyStyle
                loadState = MapLoadState.READY
            }
        }
    }

    LaunchedEffect(map, style, locationGranted) {
        val readyMap = map
        val readyStyle = style
        if (locationGranted && readyMap != null && readyStyle != null) {
            enableLocation(context, readyMap, readyStyle)
        }
    }

    Box(modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        Surface(
            modifier = Modifier.align(Alignment.TopCenter).padding(12.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
            tonalElevation = 4.dp,
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("MAP PREVIEW", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary)
                Text(descriptor.usageNotice, style = MaterialTheme.typography.bodyMedium)
                route?.let { calculated ->
                    Text(
                        if (calculated.hasCurrentDriverReview) "Route: DRIVER REVIEWED"
                        else "Route: UNVERIFIED",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (calculated.hasCurrentDriverReview) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.error,
                    )
                    if (calculated.warnings.isNotEmpty()) Text("Route: DATA WARNING",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error)
                    if (System.currentTimeMillis() - calculated.provenance.receivedAtEpochMillis >
                        24 * 60 * 60 * 1000L
                    ) Text("Route: OFFLINE / STALE", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error)
                }
                Text(
                    if (loadState == MapLoadState.READY) "Map loaded" else "Loading evaluation map…",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.CenterEnd).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SmallFloatingActionButton(
                onClick = { map?.animateCamera(CameraUpdateFactory.zoomIn()) },
            ) { Icon(Icons.Rounded.Add, "Zoom in") }
            SmallFloatingActionButton(
                onClick = { map?.animateCamera(CameraUpdateFactory.zoomOut()) },
            ) { Icon(Icons.Rounded.Remove, "Zoom out") }
            SmallFloatingActionButton(
                onClick = {
                    if (locationGranted) {
                        val readyMap = map
                        val readyStyle = style
                        if (readyMap != null && readyStyle != null) {
                            enableLocation(context, readyMap, readyStyle)
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                },
                containerColor = if (locationDenied) MaterialTheme.colorScheme.errorContainer
                    else FloatingActionButtonDefaults.containerColor,
            ) { Icon(Icons.Rounded.MyLocation, "Show approximate location") }
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Text(
                if (locationDenied) "Location permission denied · ${descriptor.attribution}"
                else descriptor.attribution,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

private fun Context.hasCoarseLocationPermission() =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED

@SuppressLint("MissingPermission")
private fun enableLocation(context: Context, map: MapLibreMap, style: Style) {
    if (!context.hasCoarseLocationPermission()) return
    val component = map.locationComponent
    if (!component.isLocationComponentActivated) {
        component.activateLocationComponent(
            LocationComponentActivationOptions.builder(context, style)
                .useDefaultLocationEngine(true)
                .build(),
        )
    }
    component.isLocationComponentEnabled = true
}
