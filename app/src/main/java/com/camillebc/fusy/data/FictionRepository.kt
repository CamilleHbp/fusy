package com.camillebc.fusy.data

import android.util.Log
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.interfaces.RepositoryInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import javax.inject.Singleton

private const val TAG = APP_TAG + "FictionRepository"
internal const val ROYALROAD = "royalroad"
internal const val FANFICTION = "fanfiction"

@Singleton
class FictionRepository(
    private val database: FictionDatabase,
    private val host: FictionHostInterface,
    private val hardwareStatusManager: HardwareStatusManager
): RepositoryInterface<Fiction> {

    fun getAll(): List<Fiction> {
        return database.fictionDao().getAllFictions()
    }

    override fun getById(id: Long): Fiction {
        return database.fictionDao().getFictionById(id)
    }

    override fun add(item: Fiction) {
        database.fictionDao().insertFiction(item)
    }

    override fun delete(item: Fiction) {
        database.fictionDao().deleteFiction(item)
    }

    override fun edit(item: Fiction) {
        database.fictionDao().updateFiction(item)
    }

    suspend fun getFiction(id: Long, hostId: String): Fiction? {
        return if (hardwareStatusManager.getConnectivityStatus() != HardwareStatusManager.InternetStatus.OFFLINE) {
            Log.i(TAG, "Connectivity status: ${hardwareStatusManager.getConnectivityStatus().name}")
            Log.i(TAG, "Getting fiction from Host")
            val fiction = getFictionFromHost(id, hostId)
            // TODO() // Create a diff to see if there is a need to insert in Db
            if (fiction != null) database.fictionDao().insertFiction(fiction)
            fiction
        } else {
            Log.i(TAG, "Getting favourites from Db")
            database.fictionDao().getFictionById(id)
        }
    }

    suspend fun getFavourites(hostId: String): List<Fiction> {
        if (hardwareStatusManager.getConnectivityStatus() != HardwareStatusManager.InternetStatus.OFFLINE) {
            Log.i(TAG, "Connectivity status: ${hardwareStatusManager.getConnectivityStatus().name}")
            Log.i(TAG, "Getting favourites from Host")
            val favouriteList = getFavouritesFromHost(hostId)
            // TODO() // Create a diff to see if there is a need to insert in Db
            database.fictionDao().insertFictions(favouriteList)
            return favouriteList
        }
        Log.i(TAG, "Getting favourites from Db")
        return database.fictionDao().getFavourites()
    }

    private suspend fun getFictionFromHost(id: Long, hostId: String): Fiction? = when (hostId) {
        ROYALROAD -> host.getFiction(id)
        FANFICTION -> null // TODO("To implement")
        else -> null
    }
    private suspend fun getFavouritesFromHost(hostId: String): List<Fiction> = when (hostId) {
        ROYALROAD -> host.getFavourites()
        FANFICTION -> emptyList() // TODO("To implement")
        else -> emptyList()
    }
}
