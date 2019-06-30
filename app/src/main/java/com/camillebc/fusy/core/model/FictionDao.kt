package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import android.provider.SyncStateContract.Helpers.update
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.room.OnConflictStrategy.IGNORE


@Dao
interface FictionDao {
    @Query("select * from fictions")
    suspend fun getAllFictions(): List<Fiction>

    @Query("select * from fictions where favourite = :favourite")
    suspend fun getFavourites(favourite: Boolean = true): List<Fiction>

    @Query("select * from fictions where fiction_id = :fictionId and provider = :provider limit 1")
    suspend fun getFictionById(fictionId: String, provider: String): Fiction?

    @Query("select * from fictions where category_name = :category")
    suspend fun getFictionsByCategory(category: String?): List<Fiction>?

    @Query("select * from fictions where category_name is null")
    suspend fun getFictionsByDefaultCategory(): List<Fiction>?

    @Query("select * from fictions where provider = :provider")
    suspend fun getFictionsByProvider(provider: String): List<Fiction>?

    @Query("select * from fictions where name = :name limit 1")
    suspend fun getFictionByTitle(name: String): Fiction?

    @Transaction
    suspend fun upsertFiction(fiction: Fiction) {
        if (insertFiction(fiction) == -1L) updateFiction(fiction)
    }

    @Transaction
    suspend fun upsertFictions(fictions: List<Fiction>) {
        val results = insertFictions(fictions)
        val updateList = mutableListOf<Fiction>().apply {
            results.forEachIndexed { index: Int, result: Long -> if (result == -1L) add(fictions[index]) }
        }
        if (updateList.isNotEmpty()) updateFictions(updateList)
    }

    @Insert(onConflict = IGNORE)
    suspend fun insertFiction(fiction: Fiction): Long

    @Insert(onConflict = IGNORE)
    suspend fun insertFictions(fictionsList: List<Fiction>): List<Long>

    @Update(onConflict = IGNORE)
    suspend fun updateFiction(fiction: Fiction)

    @Update(onConflict = IGNORE)
    suspend fun updateFictions(fictionsList: List<Fiction>)

    @Delete
    suspend fun deleteFiction(fiction: Fiction)
}