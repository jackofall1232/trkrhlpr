package com.lastwagon.app

import android.app.Application
import com.lastwagon.core.data.*
import com.lastwagon.core.model.*
import kotlinx.coroutines.*
import com.lastwagon.feature.routing.FileRouteRepository
import com.lastwagon.feature.routing.GeocodingProvider
import com.lastwagon.feature.routing.KeyOverrideGeocodingProvider
import com.lastwagon.feature.routing.KeyOverrideRoutingProvider
import com.lastwagon.feature.routing.MapStyleProvider
import com.lastwagon.feature.routing.NetworkMonitor
import com.lastwagon.feature.routing.OfflineCorridorManager
import com.lastwagon.feature.routing.OpenFreeMapLibertyStyleProvider
import kotlinx.coroutines.flow.first

class LastWagonApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            container.contentRepository.ensureSampleContent()
        }
    }
}

class AppContainer(application: Application) {
    private val database = LastWagonDatabase.create(application)
    val contentRepository: ContentRepository = OfflineContentRepository(database)
    val progressRepository: ProgressRepository = OfflineProgressRepository(database.dao())
    val preferencesRepository: PreferencesRepository = DataStorePreferencesRepository(application)
    val vehicleProfileRepository: VehicleProfileRepository = DataStoreVehicleProfileRepository(application)
    // A driver-supplied key from Settings overrides the build-time key per request, so a
    // rotated or exhausted baked key can be replaced without redistributing the APK.
    private val orsKeyOverride: suspend () -> String = {
        preferencesRepository.preferences.first().orsApiKeyOverride
    }
    val routingProvider: RoutingProvider =
        KeyOverrideRoutingProvider(BuildConfig.ORS_API_KEY, orsKeyOverride)
    val geocodingProvider: GeocodingProvider =
        KeyOverrideGeocodingProvider(BuildConfig.ORS_API_KEY, orsKeyOverride)
    val routeRepository: RouteRepository = FileRouteRepository(application)
    // Corridor prefetch stays disabled outside debug/testing builds until the provider's
    // offline-storage terms are confirmed in writing (docs/map-provider-evaluation.md).
    val mapStyleProvider: MapStyleProvider =
        OpenFreeMapLibertyStyleProvider(developmentPrefetchEnabled = BuildConfig.DEBUG)
    val networkMonitor = NetworkMonitor(application)
    val corridorManager = OfflineCorridorManager(application)
}
