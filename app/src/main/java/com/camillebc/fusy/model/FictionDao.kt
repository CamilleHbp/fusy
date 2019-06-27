package com.camillebc.fusy.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionDao {
    @Query("select * from fiction")
    fun getAllFictions(): List<FictionMetadataEntity>

    @Query("select * from fiction where favourite = :favoured")
    fun getFavourites(favoured: Boolean = true): List<FictionMetadataEntity>

    @Query("select * from fiction where provider = :hostId")
    fun getFictionByHostId(hostId: Long): List<FictionMetadataEntity>

    @Query("select * from fiction where id = :id limit 1")
    fun getFictionById(id: Long): FictionMetadataEntity?

    @Query("select * from fiction where name = :name limit 1")
    fun getFictionByTitle(name: String): FictionMetadataEntity?

    @Insert(onConflict = REPLACE)
    fun insertFiction(fictionMetadataEntity: FictionMetadataEntity)

    @Insert(onConflict = REPLACE)
    fun insertFictions(fictionsList: List<FictionMetadataEntity>)

    @Update(onConflict = REPLACE)
    fun updateFiction(fictionMetadataEntity: FictionMetadataEntity)

    @Update(onConflict = REPLACE)
    fun updateFictions(fictionsList: List<FictionMetadataEntity>)

    @Delete
    fun deleteFiction(fictionMetadataEntity: FictionMetadataEntity)
}