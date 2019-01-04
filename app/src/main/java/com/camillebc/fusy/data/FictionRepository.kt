package com.camillebc.fusy.data

import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.interfaces.FictionHostInterface
import kotlinx.coroutines.*
import javax.inject.Singleton

@Singleton
class FictionRepository(fictionDatabase: FictionDatabase, host: FictionHostInterface) {
    private val database = fictionDatabase
    val host = host

    suspend fun getFavourites(): List<Fiction> = host.getFavourites()

    fun login(username: String, password: String, isLoggedIn: MutableLiveData<Boolean>) =
        host.login(username, password, isLoggedIn)
}
