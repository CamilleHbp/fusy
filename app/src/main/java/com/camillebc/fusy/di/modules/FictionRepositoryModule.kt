package com.camillebc.fusy.di.modules

import com.camillebc.fusy.model.FictionDatabase
import com.camillebc.fusy.model.FictionRepository
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
        providers: List<FictionProviderApi>,
        fictionDatabase: FictionDatabase,
        hardwareStatusManager: HardwareStatusManager
    ) = FictionRepository(providers, fictionDatabase, hardwareStatusManager)
}