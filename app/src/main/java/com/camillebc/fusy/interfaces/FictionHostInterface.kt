package com.camillebc.fusy.interfaces

import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.Tag

interface FictionHostInterface {
    suspend fun login(username: String, password: String): Boolean
    suspend fun getFavourites(): List<Fiction>
    suspend fun getFiction(hostId: Long): Fiction?
    suspend fun getFictionTags(hostId: Long): List<Tag>
    suspend fun search(query: String): List<Fiction>
}