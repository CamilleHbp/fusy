package com.camillebc.fusy.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionDao {
    @Query("select * from fiction")
    fun getAllFictions(): List<FictionForDb>

    @Query("select * from fiction where favourite = :favoured")
    fun getFavourites(favoured: Boolean = true): List<FictionForDb>

    @Query("select * from fiction where host_id = :hostId limit 1")
    fun getFictionByHostId(hostId: Long): FictionForDb

    @Query("select * from fiction where id = :id limit 1")
    fun getFictionById(id: Long): FictionForDb

    @Query("select * from fiction where name = :name limit 1")
    fun getFictionByTitle(name: String): FictionForDb

    @Insert(onConflict = REPLACE)
    fun insertFiction(fictionForDb: FictionForDb)

    @Insert(onConflict = REPLACE)
    fun insertFictions(fictionsList: List<FictionForDb>)

    @Update(onConflict = REPLACE)
    fun updateFiction(fictionForDb: FictionForDb)

    @Update(onConflict = REPLACE)
    fun updateFictions(fictionsList: List<FictionForDb>)

    @Delete
    fun deleteFiction(fictionForDb: FictionForDb)
}