package com.trkrhlpr.app

import android.app.Application
import com.trkrhlpr.core.data.*
import com.trkrhlpr.core.model.*
import kotlinx.coroutines.*

class TrkrHlprApplication : Application() {
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
    private val database = TrkrHlprDatabase.create(application)
    val contentRepository: ContentRepository = OfflineContentRepository(database)
    val progressRepository: ProgressRepository = OfflineProgressRepository(database.dao())
    val preferencesRepository: PreferencesRepository = DataStorePreferencesRepository(application)
}

