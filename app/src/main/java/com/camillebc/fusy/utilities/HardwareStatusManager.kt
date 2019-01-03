package com.camillebc.fusy.utilities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.di.modules.FictionRepositoryModule


class HardwareStatusManager(private val context: Context) {
    enum class InternetStatus { OFFLINE, RESTRICTED, UNRESTRICTED }
    enum class BatteryStatus { DISCHARGING, CHARGING }

    fun getConnectivityStatus(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo.isConnected
    }

    fun getConnectivityType(): InternetStatus {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if ( networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
            if ( networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)){
                return InternetStatus.UNRESTRICTED
            }
            return InternetStatus.RESTRICTED
        }
        return InternetStatus.OFFLINE
    }

    fun getBatteryStatus(): BatteryStatus {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

        if (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL) {
            return BatteryStatus.CHARGING
        }
        return BatteryStatus.DISCHARGING
    }
}