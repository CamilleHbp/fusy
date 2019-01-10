package com.camillebc.fusy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag")
data class Tag
    (
    val name: String,
    val warning: Boolean = false

){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}