package com.camillebc.fusy.di.modules

import com.camillebc.fusy.model.FictionDatabase
import com.camillebc.fusy.model.FictionRepository
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.HardwareStatusManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionRepositoryModule {
    @Provides
    @Singleton
    fun provideFictionRepository(fictionDatabase: FictionDatabase, host: FictionHostInterface, hardwareStatusManager: HardwareStatusManager) = FictionRepository(fictionDatabase, host, hardwareStatusManager)
}