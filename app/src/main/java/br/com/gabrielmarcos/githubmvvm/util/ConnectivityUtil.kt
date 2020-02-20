package br.com.gabrielmarcos.githubmvvm.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object ConnectivityUtil {
    @Deprecated("Use rx to verify connection in runtime")
    fun isConnected(context: Context): Boolean {
        val connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}