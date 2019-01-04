package com.camillebc.fusy.interfaces

import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.data.Fiction

interface FictionHostInterface {
    fun login(username: String, password: String, isConnected: MutableLiveData<Boolean>)
    suspend fun getFavouritesOrNull(): List<Fiction>?
    fun updateReading(readingList: MutableLiveData<List<Fiction>>)
}