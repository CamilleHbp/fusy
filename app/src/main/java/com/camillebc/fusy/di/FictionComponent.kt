package com.camillebc.fusy.di

import com.camillebc.fusy.*
import com.camillebc.fusy.di.modules.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    FictionDatabaseModule::class,
    FictionRepositoryModule::class,
    HardwareStatusModule::class
])
interface FictionComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(searchableActivity: SearchableActivity)
}