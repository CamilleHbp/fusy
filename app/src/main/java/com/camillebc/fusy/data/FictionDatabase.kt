package com.camillebc.fusy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(entities = [Fiction::class, Tag::class, FictionTagJoin::class], version = 4)
abstract class FictionDatabase: RoomDatabase() {
    abstract fun fictionDao(): FictionDao
    abstract fun tagDao(): TagDao
    abstract fun fictionTagJoinDao(): FictionTagJoinDao
}