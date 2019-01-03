package com.camillebc.fusy.di.modules

import android.content.Context
import androidx.room.Room
import com.camillebc.fusy.data.FictionDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionDatabaseModule {
    @Provides @Singleton
    fun providesFictionDatabase(context: Context): FictionDatabase =
        Room.databaseBuilder(context, FictionDatabase::class.java, "fiction-db").build()
}