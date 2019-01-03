package com.camillebc.fusy.di.modules

import android.content.Context
import com.camillebc.fusy.utilities.HardwareStatusManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HardwareStatusModule {
    @Provides @Singleton
    fun providesHardwareStatusManager(context: Context) = HardwareStatusManager(context)
}