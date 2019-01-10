package com.camillebc.fusy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "fiction")
data class Fiction(
    val name: String,
    val host: String,
    @ColumnInfo(name = "host_id")
    val hostId: Long,
    val author: String = "unknown",
    val description: String = "No description.",
    val favourite: Boolean = false,
    @ColumnInfo(name = "image_url")
    val imageUrl: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
