package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import me.camillebc.fictionproviderapi.Chapter

@Dao
interface ChapterDao {
    @Query("select * from chapters where fiction_id = :fictionId")
    suspend fun getFictionChapters(fictionId: Long): List<Chapter>

    @Query("select * from categories where id = :id limit 1")
    suspend fun getChapter(id: Long): Chapter

    @Insert(onConflict = REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Insert(onConflict = REPLACE)
    suspend fun insertChapters(chapters: List<Chapter>)

    @Update(onConflict = REPLACE)
    suspend fun updateChapter(chapter: Chapter)

    @Update(onConflict = REPLACE)
    suspend fun updateChapters(chapters: List<Chapter>)

    @Delete
    suspend fun deleteChapter(chapter: Chapter)
}