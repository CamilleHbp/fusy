package com.camillebc.fusy.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Fiction::class], version = 1)
abstract class FictionDatabase: RoomDatabase() {
    abstract fun fictionDao(): FictionDao
}