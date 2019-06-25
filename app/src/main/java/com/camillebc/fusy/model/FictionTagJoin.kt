package com.camillebc.fusy.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "fiction_tag_join",
    primaryKeys = ["fictionId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = FictionForDb::class,
            parentColumns = ["id"],
            childColumns = ["fictionId"],
            onDelete = ForeignKey.NO_ACTION),
        ForeignKey(entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.NO_ACTION)
    ])
class FictionTagJoin(
    val fictionId: Long,
    val tagId: Long
)
