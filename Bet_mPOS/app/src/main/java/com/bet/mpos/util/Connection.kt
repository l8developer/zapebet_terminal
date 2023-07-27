package com.bet.mpos.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class Connection {

    fun getConnectionType(context: Context): String {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return "Sem conexão de rede"

            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return "Sem conexão de rede"

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return "Wi-Fi"
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return "Dados móveis"
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                val type = networkInfo.type
                if (type == ConnectivityManager.TYPE_WIFI) {
                    return "Wi-Fi"
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    return "Dados móveis"
                }
            }
        }

        return "Desconhecido"
    }
}