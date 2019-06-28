package com.camillebc.fusy.core.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import me.camillebc.fictionproviderapi.Chapter

@Dao
interface ChapterDao {
    @Query("select * from chapters where fiction_id = :fictionId")
    fun getFictionChapters(fictionId: Long): List<Chapter>

    @Query("select * from categories where id = :id limit 1")
    fun getChapter(id: Long): Chapter

    @Insert(onConflict = REPLACE)
    fun insertChapter(chapter: Chapter)

    @Insert(onConflict = REPLACE)
    fun insertChapters(chapters: List<Chapter>)

    @Update(onConflict = REPLACE)
    fun updateChapter(chapter: Chapter)

    @Update(onConflict = REPLACE)
    fun updateChapters(chapters: List<Chapter>)

    @Delete
    fun deleteChapter(chapter: Chapter)
}