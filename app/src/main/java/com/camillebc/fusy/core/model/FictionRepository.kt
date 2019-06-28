package com.camillebc.fusy.core.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.camillebc.fictionproviderapi.ApiProvider
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Singleton

@Singleton
class FictionRepository(
    private val database: FictionDatabase,
    private val hardwareStatusManager: HardwareStatusManager
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val providers = ApiProvider.getAllApi()

    fun getAll(): List<Fiction> {
        return runBlocking {
            database.fictionDao().getAllFictions()
        }
    }

    fun getById(id: String): Fiction? {
        return runBlocking {
            database.fictionDao().getFictionById(id)
        }
    }

    suspend fun add(item: Fiction) = database.fictionDao().insertFiction(item)

    suspend fun delete(item: Fiction) = database.fictionDao().deleteFiction(item)

    suspend fun update(item: Fiction) = database.fictionDao().updateFiction(item)

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun search(
        query: String? = null,
        name: String? = null,
        author: String? = null,
        tags: List<Tag>? = null,
        local: Boolean = false
    ): ReceiveChannel<Fiction> = produce {
        TODO("Implement a local db and online search") // Should rely on the connection status
    }
}
