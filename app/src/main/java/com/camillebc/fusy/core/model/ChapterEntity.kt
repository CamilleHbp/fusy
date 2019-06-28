package com.camillebc.fusy.core.model

import androidx.room.*

@Entity(
    tableName = "chapters",
    indices = [Index("fiction_id")],
    foreignKeys = [
        ForeignKey(
            entity = Fiction::class,
            parentColumns = ["id"],
            childColumns = ["fiction_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class ChapterEntity(@PrimaryKey val id: String, var path: String, @ColumnInfo(name = "fiction_id") val fictionId: String)
