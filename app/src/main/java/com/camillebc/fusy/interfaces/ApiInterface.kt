package com.camillebc.fusy.interfaces

import android.arch.lifecycle.MutableLiveData

interface ApiInterface {
    fun login(username: String, password: String, isConnected: MutableLiveData<Boolean>)
}