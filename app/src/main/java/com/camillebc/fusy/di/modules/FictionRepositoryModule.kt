package com.camillebc.fusy.di.modules

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.MutableLiveData
import dagger.Module

@Module
class FictionRepositoryModule {
    enum class INTERNET_STATUS { OFFLINE, RESTRICTED, UNRESTRICTED }
    private val isConnectedRoyalroad = MutableLiveData<Boolean>().also { it.value = false }
    private val isConnectedFanfiction = MutableLiveData<Boolean>().also { it.value = false }

    private fun getConnectivityStatus(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo.isConnected
    }

    private fun getConnectivityType(context: Context): INTERNET_STATUS {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if ( networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
            if ( networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)){
                return INTERNET_STATUS.UNRESTRICTED
            }
            return INTERNET_STATUS.RESTRICTED
        }
        return INTERNET_STATUS.OFFLINE
    }
}