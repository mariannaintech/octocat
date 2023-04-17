package com.octocat.network.manager

import android.content.Context
import android.net.ConnectivityManager

class NetworkManagerImpl(
    private val context: Context
) : NetworkManager {
    override val isNetworkAvailable: Boolean
        get() {
            try {
                val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                return capabilities != null
            } catch (_: Throwable) {
                return false
            }
        }
}