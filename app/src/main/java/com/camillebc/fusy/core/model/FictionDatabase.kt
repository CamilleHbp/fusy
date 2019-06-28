package com.camillebc.fusy.core.model

import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(entities = [Fiction::class, Tag::class, FictionTagJoin::class, Category::class], version = 6)
abstract class FictionDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun fictionDao(): FictionDao
    abstract fun fictionTagJoinDao(): FictionTagJoinDao
    abstract fun tagDao(): TagDao
}