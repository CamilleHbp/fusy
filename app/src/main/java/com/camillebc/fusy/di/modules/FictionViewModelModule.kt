package com.camillebc.fusy.di.modules

import com.camillebc.fusy.data.FictionViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionViewModelModule {
    @Provides
    @Singleton
    fun providesFictionViewModel() = FictionViewModel()
}