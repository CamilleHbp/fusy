package com.camillebc.fusy.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "fiction_tag_join",
    primaryKeys = ["fictionId", "tagId"],
    indices = [Index(value = ["fictionId", "tagId"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = Fiction::class,
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
