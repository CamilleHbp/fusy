package com.camillebc.fusy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "fiction_tag_join",
    indices = [Index("fiction_id"), Index("tag_id")],
    primaryKeys = ["fiction_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = FictionMetadataEntity::class,
            parentColumns = ["id"],
            childColumns = ["fiction_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class FictionTagJoin(
    @ColumnInfo(name = "fiction_id")
    val fictionId: Long,
    @ColumnInfo(name = "tag_id")
    val tagId: Long
)
