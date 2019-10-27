package com.camillebc.fusy.core.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import me.camillebc.fictionproviderapi.ApiProvider
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Singleton

@Singleton
class FictionRepository(
    private val database: FictionDatabase,
    private val hardwareStatusManager: HardwareStatusManager
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val providers = ApiProvider.getAllApi()

    suspend fun getAll(): List<Fiction> = database.fictionDao().getAllFictions()

    suspend fun getFictionById(fictionId: String, provider: String): Fiction? =
        database.fictionDao().getFictionById(fictionId, provider)

    suspend fun getFictionsByCategory(category: String?): List<Fiction>? {
        return if (category != null) {
            database.fictionDao().getFictionsByCategory(category)
        } else {
            database.fictionDao().getFictionsByDefaultCategory()
        }
    }

    suspend fun getCategories(): List<String> =
        database.categoryDao().getAllCategories()?.map { it.name ?: "" }?.sorted() ?: listOf()

    suspend fun getTags(): List<String> =
        database.tagDao().getAllTags()?.map { it.name }?.sortedWith(compareBy { it }) ?: listOf()

    suspend fun add(item: Fiction) {
        database.fictionDao().upsertFiction(item)
    }

    suspend fun delete(item: Fiction) = database.fictionDao().deleteFiction(item)

    suspend fun update(item: Fiction) = database.fictionDao().updateFiction(item)

    suspend fun updateTags() {
        val tagList = mutableListOf<Tag>().also { list ->
            providers.forEach { provider ->
                provider.getProviderTags().distinct().forEach { tagString ->
                    if (list.none { it.name == tagString }) {
                        list.add(Tag(name = tagString))
                    }
                }
            }
        }
        database.tagDao().updateTags(tagList.distinct())
    }

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
