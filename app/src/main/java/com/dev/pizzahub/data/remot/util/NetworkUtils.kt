package com.dev.pizzahub.data.remot.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)      -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)  -> true
            //for other device how are able to connect with Ethernet
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)  -> true
            //for check internet over Bluetooth
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else                                                                -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI      -> true
                    ConnectivityManager.TYPE_MOBILE    -> true
                    ConnectivityManager.TYPE_ETHERNET  -> true
                    ConnectivityManager.TYPE_BLUETOOTH -> true
                    else                               -> false
                }
            }
        }
    }
    return false
}

