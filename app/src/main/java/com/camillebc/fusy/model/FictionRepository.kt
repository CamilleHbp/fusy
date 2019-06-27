package com.camillebc.fusy.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import me.camillebc.fictionproviderapi.FictionProviderApi
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Singleton

@Singleton
class FictionRepository(
    private val providers: List<FictionProviderApi>,
    private val database: FictionDatabase,
    private val hardwareStatusManager: HardwareStatusManager
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    fun getAll(): List<FictionMetadataEntity> {
        return database.fictionDao().getAllFictions()
    }

    fun getById(id: Long): FictionMetadataEntity? {
        return database.fictionDao().getFictionById(id)
    }

    fun add(item: FictionMetadataEntity) = database.fictionDao().insertFiction(item)

    fun delete(item: FictionMetadataEntity) = database.fictionDao().deleteFiction(item)

    fun edit(item: FictionMetadataEntity) = database.fictionDao().updateFiction(item)

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun search(
        query: String? = null,
        name: String? = null,
        author: String? = null,
        tags: List<Tag>? = null,
        local: Boolean = false
    ): ReceiveChannel<FictionMetadataEntity> = produce {
        TODO("Implement a local db and online search") // Should rely on the connection status
    }
}
