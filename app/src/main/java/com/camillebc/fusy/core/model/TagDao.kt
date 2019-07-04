package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface TagDao {
    @Query("select * from tags")
    suspend fun getAllTags(): List<Tag>?

    @Query("select * from tags where warning = :warning")
    suspend fun getWarnings(warning: Boolean = true): List<Tag>

    @Query("select * from tags where id = :id limit 1")
    suspend fun getTag(id: Long): Tag

    @Insert(onConflict = REPLACE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = REPLACE)
    suspend fun insertTags(tagList: List<Tag>)

    @Update(onConflict = REPLACE)
    suspend fun updateTag(tag: Tag)

    @Update(onConflict = REPLACE)
    suspend fun updateTags(tagList: List<Tag>)

    @Delete
    suspend fun deleteTag(tag: Tag)
}