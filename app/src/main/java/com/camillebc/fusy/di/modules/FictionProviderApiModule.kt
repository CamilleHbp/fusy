package com.camillebc.fusy.di.modules

import dagger.Module
import dagger.Provides
import me.camillebc.fictionproviderapi.ApiProvider
import javax.inject.Singleton

@Module
class FictionProviderApiModule {
    @Provides
    @Singleton
    fun providesFictionProviderApis() = ApiProvider.getAllApi()
}