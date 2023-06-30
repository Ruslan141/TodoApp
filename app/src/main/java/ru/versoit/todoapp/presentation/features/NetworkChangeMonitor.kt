package ru.versoit.todoapp.presentation.features

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkChangeMonitor(context: Context) {

    private val connectivityFlow = MutableStateFlow(false)

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            connectivityFlow.value = true
        }

        override fun onLost(network: Network) {
            super.onUnavailable()
            connectivityFlow.value = false
        }
    }

    fun startMonitoring() {

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun connectivityState(): Flow<Boolean> = connectivityFlow
}