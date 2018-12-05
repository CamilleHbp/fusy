package com.camillebc.fusy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionDatabaseModule(private val appContext: Context) {
    @Provides @Singleton fun provideDatabase()= appContext
}