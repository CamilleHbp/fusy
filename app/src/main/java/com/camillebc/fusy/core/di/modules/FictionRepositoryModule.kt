package com.camillebc.fusy.core.di.modules

import com.camillebc.fusy.core.model.FictionDatabase
import com.camillebc.fusy.core.model.FictionRepository
import dagger.Module
import dagger.Provides
import me.camillebc.fictionproviderapi.FictionProviderApi
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Singleton

@Module
class FictionRepositoryModule {
    @Provides
    @Singleton
    fun provideFictionRepository(
        fictionDatabase: FictionDatabase,
        hardwareStatusManager: HardwareStatusManager
    ) = FictionRepository(fictionDatabase, hardwareStatusManager)
}