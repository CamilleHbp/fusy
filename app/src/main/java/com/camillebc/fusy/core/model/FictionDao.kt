package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionDao {
    @Query("select * from fictions")
    suspend fun getAllFictions(): List<Fiction>

    @Query("select * from fictions where favourite = :favourite")
    suspend fun getFavourites(favourite: Boolean = true): List<Fiction>

    @Query("select * from fictions where id = :id limit 1")
    suspend fun getFictionById(id: String): Fiction?

    @Query("select * from fictions where name = :name limit 1")
    suspend fun getFictionByTitle(name: String): Fiction?

    @Insert(onConflict = REPLACE)
    suspend fun insertFiction(fiction: Fiction)

    @Insert(onConflict = REPLACE)
    suspend fun insertFictions(fictionsList: List<Fiction>)

    @Update(onConflict = REPLACE)
    suspend fun updateFiction(fiction: Fiction)

    @Update(onConflict = REPLACE)
    suspend fun updateFictions(fictionsList: List<Fiction>)

    @Delete
    suspend fun deleteFiction(fiction: Fiction)
}