package com.camillebc.fusy.di

import com.camillebc.fusy.MainActivity
import com.camillebc.fusy.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}