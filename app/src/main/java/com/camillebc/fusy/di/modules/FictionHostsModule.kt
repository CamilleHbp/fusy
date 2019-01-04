package com.camillebc.fusy.di.modules

import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.network.RoyalroadHost
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionHostsModule {
    @Provides
    @Singleton
    fun providesRoyalRoadHost(): FictionHostInterface = RoyalroadHost()
}