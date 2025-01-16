package com.demo.upstox.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class AndroidNetworkMonitor(
    context: Context,
    dispatchersProvider: DispatchersProvider
) : NetworkMonitor {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    override val isConnected: Flow<Boolean> = callbackFlow {
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        val callback = object : NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val connected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(connected)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.flowOn(dispatchersProvider.io)
}
