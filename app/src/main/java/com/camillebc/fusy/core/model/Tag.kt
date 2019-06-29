package com.camillebc.fusy.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag
    (
    val name: String,
    val warning: Boolean = false

){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}