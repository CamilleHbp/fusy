package com.camillebc.fusy.model
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionTagJoinDao {
    @Query("select * from fiction_tag_join")
    fun getAllFictionTagJoins(): List<FictionTagJoin>

    @Query("select * from tag  inner join fiction_tag_join on tag.id=fiction_tag_join.tagId where fiction_tag_join.fictionId=:fictionId")
    fun getTagsForFiction(fictionId: Long): List<Tag>

    @Query("select * from fiction  inner join fiction_tag_join on fiction.id=fiction_tag_join.fictionId where fiction_tag_join.tagId=:tagId")
    fun getFictionsForTag(tagId: Long): List<Fiction>

    @Insert(onConflict = REPLACE)
    fun insertFictionTagJoin(fictionTagJoin: FictionTagJoin)

    @Insert(onConflict = REPLACE)
    fun insertFictionTagJoins(fictionTagJoins: List<FictionTagJoin>)

    @Delete
    fun deleteFictionRepoJoin(fictionTagJoin: FictionTagJoin)
}
