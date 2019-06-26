package com.camillebc.fusy.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionDao {
    @Query("select * from fiction")
    fun getAllFictions(): List<FictionEntity>

    @Query("select * from fiction where favourite = :favoured")
    fun getFavourites(favoured: Boolean = true): List<FictionEntity>

    @Query("select * from fiction where host_id = :hostId limit 1")
    fun getFictionByHostId(hostId: Long): FictionEntity

    @Query("select * from fiction where id = :id limit 1")
    fun getFictionById(id: Long): FictionEntity

    @Query("select * from fiction where name = :name limit 1")
    fun getFictionByTitle(name: String): FictionEntity

    @Insert(onConflict = REPLACE)
    fun insertFiction(fictionEntity: FictionEntity)

    @Insert(onConflict = REPLACE)
    fun insertFictions(fictionsList: List<FictionEntity>)

    @Update(onConflict = REPLACE)
    fun updateFiction(fictionEntity: FictionEntity)

    @Update(onConflict = REPLACE)
    fun updateFictions(fictionsList: List<FictionEntity>)

    @Delete
    fun deleteFiction(fictionEntity: FictionEntity)
}