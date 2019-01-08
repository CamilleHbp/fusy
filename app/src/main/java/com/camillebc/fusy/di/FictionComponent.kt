package com.camillebc.fusy.di

import com.camillebc.fusy.AccountActivity
import com.camillebc.fusy.LoginActivity
import com.camillebc.fusy.MainActivity
import com.camillebc.fusy.SearchActivity
import com.camillebc.fusy.di.modules.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    FictionDatabaseModule::class,
    FictionHostsModule::class,
    FictionRepositoryModule::class,
    HardwareStatusModule::class
])
interface FictionComponent {
    fun inject(accountActivity: AccountActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(searchActivity: SearchActivity)
}