package com.camillebc.fusy.core.di.modules

import android.content.Context
import androidx.room.Room
import com.camillebc.fusy.core.model.FictionDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionDatabaseModule {
    @Provides
    @Singleton
    fun providesFictionDatabase(context: Context) = Room.databaseBuilder( context,
        FictionDatabase::class.java, "fictionDetail-db" )
        .fallbackToDestructiveMigration()
        .build()
}