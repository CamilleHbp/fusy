package com.camillebc.fusy.di

import com.camillebc.fusy.MainActivity
import com.camillebc.fusy.di.modules.ContextModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}