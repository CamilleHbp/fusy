package com.camillebc.fusy.di.modules

import androidx.lifecycle.MutableLiveData
import dagger.Module

@Module
class FictionRepositoryModule {
    private val isConnectedRoyalroad = MutableLiveData<Boolean>().also { it.value = false }
    private val isConnectedFanfiction = MutableLiveData<Boolean>().also { it.value = false }
}