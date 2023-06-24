package com.mad43.stylista.util

import android.app.Application
import android.content.Context
import android.net.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NetworkConnectivity private constructor(val application: Application) {

    private val networkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()

    private val _connectivitySharedFlow = MutableSharedFlow<NetworkStatus>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val connectivitySharedFlow = _connectivitySharedFlow.asSharedFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _connectivitySharedFlow.tryEmit(NetworkStatus.CONNECTED)
        }

        override fun onCapabilitiesChanged(
            network: Network, networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _connectivitySharedFlow.tryEmit(NetworkStatus.LOST)
        }
    }

    init {
        val connectivityManager =
            application.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
    fun isOnline(): Boolean {
        val connMgr = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
    companion object {
        private lateinit var instance: NetworkConnectivity
        fun getInstance(application: Application): NetworkConnectivity {
            if (!::instance.isInitialized) {
                instance = NetworkConnectivity(application)
            }
            return instance
        }
    }

}

enum class NetworkStatus {
    CONNECTED, LOST
}