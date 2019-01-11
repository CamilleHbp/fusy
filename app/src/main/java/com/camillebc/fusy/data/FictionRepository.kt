package com.camillebc.fusy.data

import android.util.Log
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.interfaces.RepositoryInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import javax.inject.Singleton

private const val TAG = APP_TAG + "FictionRepository"
@Singleton
class FictionRepository(
    private val database: FictionDatabase,
    private val host: FictionHostInterface,
    private val hardwareStatusManager: HardwareStatusManager
): RepositoryInterface<Fiction> {

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

    suspend fun updateFavourites(hostIds: List<Long>) {
        if (hardwareStatusManager.getConnectivityStatus() != HardwareStatusManager.InternetStatus.OFFLINE) {
            Log.i(TAG, "Connectivity status: ${hardwareStatusManager.getConnectivityStatus().name}")
            Log.i(TAG, "Getting favourites from Host")
            val favourites = host.getFavourites()
            // TODO() // Create a diff to see if there is a need to insert in Db
//            database.fictionDao().insertFictions(fictionList)
//            return fictionList
        }
        Log.i(TAG, "Getting favourites from Db")
//        return database.fictionDao().getFavourites()
    }
}
