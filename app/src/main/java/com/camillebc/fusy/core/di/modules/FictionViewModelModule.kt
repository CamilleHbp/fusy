package com.camillebc.fusy.core.di.modules

import com.camillebc.fusy.core.model.FictionViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FictionViewModelModule {
    @Provides
    @Singleton
    fun providesFictionViewModel() = FictionViewModel()
}