package com.camillebc.fusy.interfaces

import com.camillebc.fusy.model.FictionForDb
import com.camillebc.fusy.model.Tag

interface FictionHostInterface {
    suspend fun login(username: String, password: String): Boolean
    suspend fun getFavourites(): List<FictionForDb>
    suspend fun getFiction(hostId: Long): FictionForDb?
    suspend fun getFictionTags(hostId: Long): List<Tag>
    suspend fun search(query: String): List<FictionForDb>
}