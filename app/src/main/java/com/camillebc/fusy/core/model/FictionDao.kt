package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionDao {
    @Query("select * from fictions")
    fun getAllFictions(): List<Fiction>

    @Query("select * from fictions where favourite = :favoured")
    fun getFavourites(favoured: Boolean = true): List<Fiction>

    @Query("select * from fictions where provider = :hostId")
    fun getFictionByHostId(hostId: Long): List<Fiction>

    @Query("select * from fictions where id = :id limit 1")
    fun getFictionById(id: Long): Fiction?

    @Query("select * from fictions where name = :name limit 1")
    fun getFictionByTitle(name: String): Fiction?

    @Insert(onConflict = REPLACE)
    fun insertFiction(fiction: Fiction)

    @Insert(onConflict = REPLACE)
    fun insertFictions(fictionsList: List<Fiction>)

    @Update(onConflict = REPLACE)
    fun updateFiction(fiction: Fiction)

    @Update(onConflict = REPLACE)
    fun updateFictions(fictionsList: List<Fiction>)

    @Delete
    fun deleteFiction(fiction: Fiction)
}