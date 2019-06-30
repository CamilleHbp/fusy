package com.camillebc.fusy.core.model
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FictionTagJoinDao {
    @Query("select * from fiction_tag_join")
    suspend fun getAllFictionTagJoins(): List<FictionTagJoin>

    @Query("select * from tags  inner join fiction_tag_join on tags.id=fiction_tag_join.tag_id where fiction_tag_join.fiction_id=:fictionId")
    suspend fun getTagsForFiction(fictionId: Long): List<Tag>

    @Query("select * from fictions  inner join fiction_tag_join on fictions.id=fiction_tag_join.fiction_id where fiction_tag_join.tag_id=:tagId")
    suspend fun getFictionsForTag(tagId: Long): List<Fiction>

    @Insert(onConflict = REPLACE)
    suspend fun insertFictionTagJoin(fictionTagJoin: FictionTagJoin)

    @Insert(onConflict = REPLACE)
    suspend fun insertFictionTagJoins(fictionTagJoins: List<FictionTagJoin>)

    @Delete
    suspend fun deleteFictionRepoJoin(fictionTagJoin: FictionTagJoin)
}
