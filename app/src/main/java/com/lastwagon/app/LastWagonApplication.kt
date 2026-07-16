package com.lastwagon.app

import android.app.Application
import com.lastwagon.core.data.*
import com.lastwagon.core.model.*
import kotlinx.coroutines.*
import com.lastwagon.feature.routing.OrsRoutingProvider
import com.lastwagon.feature.routing.FileRouteRepository

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
    val routeRepository: RouteRepository = FileRouteRepository(application)
}
