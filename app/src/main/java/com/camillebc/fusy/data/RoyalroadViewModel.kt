package com.camillebc.fusy.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

object RoyalroadViewModel: ViewModel() {
    val isConnected = MutableLiveData<Boolean>()
    val cookieManager = MutableLiveData<String>()
    val favoriteList = MutableLiveData<List<FictionData>>()
}
