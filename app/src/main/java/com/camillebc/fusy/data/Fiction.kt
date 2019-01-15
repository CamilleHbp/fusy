package com.camillebc.fusy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fiction",
    indices = [Index(value = ["id", "host"], unique = true)]
)
data class Fiction(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val host: String,
    val author: String = "unknown",
    @ColumnInfo(name = "author_id")
    val authorId: Long? = null,
    val description: String = "No description.",
    val favourite: Boolean = false,
    @ColumnInfo(name = "image_url")
    val imageUrl: String = ""
)
