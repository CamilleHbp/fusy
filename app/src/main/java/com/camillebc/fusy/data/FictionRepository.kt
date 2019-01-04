package com.camillebc.fusy.data

import com.camillebc.fusy.interfaces.FictionHostInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FictionRepository {
    private val database: FictionDatabase
    val host: FictionHostInterface

    @Inject
    constructor(fictionDatabase: FictionDatabase, host: FictionHostInterface) {
        this.database = fictionDatabase
        this.host = host
    }
}