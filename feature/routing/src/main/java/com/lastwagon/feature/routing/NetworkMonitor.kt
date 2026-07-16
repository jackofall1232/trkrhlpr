package com.lastwagon.feature.routing

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Reports whether any network with internet capability is available so the routing UI
 * can mark saved routes and corridors as offline. Absence of connectivity never hides
 * saved data — it only changes the displayed route state.
 */
class NetworkMonitor(context: Context) {
    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _online = MutableStateFlow(currentlyOnline())
    val online: StateFlow<Boolean> = _online

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _online.value = true
                }

                override fun onLost(network: Network) {
                    _online.value = currentlyOnline()
                }
            },
        )
    }

    private fun currentlyOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
