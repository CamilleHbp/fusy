package com.camillebc.fusy.di

import com.camillebc.fusy.AccountActivity
import com.camillebc.fusy.di.modules.ContextModule
import com.camillebc.fusy.di.modules.FictionDatabaseModule
import com.camillebc.fusy.di.modules.HardwareStatusModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, FictionDatabaseModule::class, HardwareStatusModule::class])
interface FictionComponent {
    fun inject(accountActivity: AccountActivity)
}