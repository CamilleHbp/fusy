package com.camillebc.fusy.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object RoyalroadViewModel: ViewModel() {
    val isConnected = MutableLiveData<Boolean>()
    val cookieManager = MutableLiveData<String>()
    val favoriteList = MutableLiveData<List<Fiction>>()
}
