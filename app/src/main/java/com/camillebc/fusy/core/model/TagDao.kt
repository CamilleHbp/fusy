package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface TagDao {
    @Query("select * from tags")
    suspend fun getAllTags(): List<Tag>?

    @Query("select * from tags where id = :id limit 1")
    suspend fun getTag(id: Long): Tag

    @Insert(onConflict = IGNORE)
    suspend fun insertTag(tag: Tag): Long

    @Insert(onConflict = IGNORE)
    suspend fun insertTags(tagList: List<Tag>): List<Long>

    @Update(onConflict = REPLACE)
    suspend fun updateTag(tag: Tag)

    @Update(onConflict = REPLACE)
    suspend fun updateTags(tagList: List<Tag>)
 @Transaction
    suspend fun upsertTag(tag: Tag) {
        if (insertTag(tag) == -1L) updateTag(tag)
    }

    @Transaction
    suspend fun upsertTags(tags: List<Tag>) {
        val results = insertTags(tags)
        val updateList = mutableListOf<Tag>().apply {
            results.forEachIndexed { index: Int, result: Long -> if (result == -1L) add(tags[index]) }
        }
        if (updateList.isNotEmpty()) updateTags(updateList)
    }

    @Delete
    suspend fun deleteTag(tag: Tag)
}