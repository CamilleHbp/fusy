package com.camillebc.fusy.interfaces

import com.camillebc.fusy.model.Fiction
import com.camillebc.fusy.model.Tag

interface FictionHostInterface {
    suspend fun login(username: String, password: String): Boolean
    suspend fun getFavourites(): List<Fiction>
    suspend fun getFiction(hostId: Long): Fiction?
    suspend fun getFictionTags(hostId: Long): List<Tag>
    suspend fun search(query: String): List<Fiction>
}