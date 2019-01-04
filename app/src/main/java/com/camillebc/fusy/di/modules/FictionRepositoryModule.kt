package com.camillebc.fusy.di.modules

import com.camillebc.fusy.data.FictionDatabase
import com.camillebc.fusy.data.FictionRepository
import com.camillebc.fusy.interfaces.FictionHostInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionRepositoryModule {
    @Provides
    @Singleton
    fun provideFictionRepository(fictionDatabase: FictionDatabase, host: FictionHostInterface) = FictionRepository(fictionDatabase, host)
}