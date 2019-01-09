package com.camillebc.fusy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(entities = [Fiction::class], version = 2)
abstract class FictionDatabase: RoomDatabase() {
    abstract fun fictionDao(): FictionDao
}