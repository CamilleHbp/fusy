package com.camillebc.fusy.model

import com.camillebc.fusy.interfaces.RepositoryInterface
import com.camillebc.fusy.utilities.APP_TAG
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Singleton

private const val TAG = APP_TAG + "FictionRepository"

@Singleton
class FictionRepository(
    private val database: FictionDatabase,
    private val hardwareStatusManager: HardwareStatusManager
) : RepositoryInterface<FictionEntity> {

    fun getAll(): List<FictionEntity> {
        return database.fictionDao().getAllFictions()
    }

    override fun getById(id: Long): FictionEntity {
        return database.fictionDao().getFictionById(id)
    }

    override fun add(item: FictionEntity) {
        database.fictionDao().insertFiction(item)
    }

    override fun delete(item: FictionEntity) {
        database.fictionDao().deleteFiction(item)
    }

    override fun edit(item: FictionEntity) {
        database.fictionDao().updateFiction(item)
    }

    suspend fun updateFavourites(hostIds: List<Long>) {
//        if (hardwareStatusManager.getConnectivityStatus(activity) != HardwareStatusManager.InternetStatus.OFFLINE) {
//            Log.i(TAG, "Connectivity status: ${hardwareStatusManager.getConnectivityStatus().name}")
//            Log.i(TAG, "Getting favourites from Host")
//            val favourites = host.getFavourites()
        // TODO() // Create a diff to see if there is a need to insert in Db
//            database.fictionDao().insertFictions(fictionList)
//            return fictionList
//        }
    }
}
