package com.camillebc.fusy.interfaces

interface RepositoryInterface<T> {
    fun getById(id: Long): T
    fun add(item: T)
    fun delete(item: T)
    fun edit(item: T)
}