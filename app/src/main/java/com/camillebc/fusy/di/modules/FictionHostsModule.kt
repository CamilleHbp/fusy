package com.camillebc.fusy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionHostsModule(private val appContext: Context) {
    @Provides @Singleton fun provideApplicationContext()= appContext
}