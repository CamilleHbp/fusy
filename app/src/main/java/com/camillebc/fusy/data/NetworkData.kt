package com.camillebc.fusy.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class NetworkViewModel: ViewModel() {
    val cookieManager = MutableLiveData<String>()
    val favorites = MutableLiveData<String>()
    val token = MutableLiveData<String>()
}