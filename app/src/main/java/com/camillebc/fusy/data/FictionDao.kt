package com.camillebc.fusy.data

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionDao {
    @Query("select * from fiction")
    fun getAllFictions(): List<Fiction>

    @Query("select * from fiction where favourite = :favoured")
    fun getFavourites(favoured: Boolean = true): List<Fiction>

    @Query("select * from fiction where host_id = :hostId limit 1")
    fun getFictionByHostId(hostId: Long): Fiction

    @Query("select * from fiction where id = :id limit 1")
    fun getFictionById(id: Long): Fiction

    @Query("select * from fiction where name = :name limit 1")
    fun getFictionByTitle(name: String): Fiction

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