package com.camillebc.fusy.model

import androidx.room.*

@Entity(
    tableName = "chapter",
    indices = [Index("fiction_id")],
    foreignKeys = [
        ForeignKey(
            entity = FictionMetadataEntity::class,
            parentColumns = ["id"],
            childColumns = ["fictionMetadataId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class ChapterEntity(@PrimaryKey val id: String, var content: List<String>, @ColumnInfo(name = "fiction_id") val fictionId: String)
