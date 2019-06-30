package com.camillebc.fusy.core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class Category(val name: String?) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
