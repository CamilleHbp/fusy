package com.camillebc.fusy.model

import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(entities = [FictionEntity::class, Tag::class, FictionTagJoin::class], version = 5)
abstract class FictionDatabase: RoomDatabase() {
    abstract fun fictionDao(): FictionDao
    abstract fun tagDao(): TagDao
    abstract fun fictionTagJoinDao(): FictionTagJoinDao
}