package com.camillebc.fusy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fiction(
    val title: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    val description: String,
    val favourite: Boolean = false,
    val host: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
