package com.lastwagon.app

import android.app.Application
import com.lastwagon.core.data.*
import com.lastwagon.core.model.*
import kotlinx.coroutines.*
import com.lastwagon.feature.routing.FileRouteRepository
import com.lastwagon.feature.routing.GeocodingProvider
import com.lastwagon.feature.routing.MapStyleProvider
import com.lastwagon.feature.routing.NetworkMonitor
import com.lastwagon.feature.routing.OfflineCorridorManager
import com.lastwagon.feature.routing.OpenFreeMapLibertyStyleProvider
import com.lastwagon.feature.routing.OrsGeocodingProvider
import com.lastwagon.feature.routing.OrsRoutingProvider

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
    val routingProvider: RoutingProvider = OrsRoutingProvider(BuildConfig.ORS_API_KEY)
    val geocodingProvider: GeocodingProvider = OrsGeocodingProvider(BuildConfig.ORS_API_KEY)
    val routeRepository: RouteRepository = FileRouteRepository(application)
    // Corridor prefetch stays disabled outside debug/testing builds until the provider's
    // offline-storage terms are confirmed in writing (docs/map-provider-evaluation.md).
    val mapStyleProvider: MapStyleProvider =
        OpenFreeMapLibertyStyleProvider(developmentPrefetchEnabled = BuildConfig.DEBUG)
    val networkMonitor = NetworkMonitor(application)
    val corridorManager = OfflineCorridorManager(application)
}
