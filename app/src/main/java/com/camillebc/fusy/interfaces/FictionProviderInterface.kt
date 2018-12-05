package com.camillebc.fusy.interfaces

import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.data.FictionData

interface FictionProviderInterface {
    fun login(username: String, password: String, isConnected: MutableLiveData<Boolean>)
    fun updateFavourites(favouritesList: MutableLiveData<List<FictionData>>)
    fun updateReading(readingList: MutableLiveData<List<FictionData>>)
}