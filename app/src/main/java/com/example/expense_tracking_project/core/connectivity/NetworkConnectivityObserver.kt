package com.example.expense_tracking_project.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkConnectivityObserver(
    context: Context
) {
    // create connectivity manager ( take the connectivity status )
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observe(): Flow<String> = callbackFlow { // flow for real time changes
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend("Available")
            }

            override fun onUnavailable() {
                trySend("Unavailable")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                trySend("Losing")
            }

            override fun onLost(network: Network) {
                trySend("Lost")
            }
        }

        val networkRequest = NetworkRequest.Builder() // type of network we want request
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        // register network call back
        connectivityManager.registerNetworkCallback(networkRequest, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}