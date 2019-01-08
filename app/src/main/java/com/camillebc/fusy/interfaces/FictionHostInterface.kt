package com.camillebc.fusy.interfaces

import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.data.Fiction

interface FictionHostInterface {
    suspend fun login(username: String, password: String): Boolean
    suspend fun getFavourites(): List<Fiction>
    suspend fun search(query: String): List<Fiction>
    fun updateReading(readingList: MutableLiveData<List<Fiction>>)
}