package com.camillebc.fusy.core.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.camillebc.fusy.core.di.Injector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.camillebc.fictionproviderapi.FictionMetadata
import javax.inject.Inject

class FictionViewModel : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.IO) {
    @Inject
    lateinit var repository: FictionRepository
    val fictionSearchList = MutableLiveData<MutableList<FictionMetadata>>(mutableListOf())
    val fictionBookshelfList = MutableLiveData<MutableList<Fiction>>(mutableListOf())
    val fictionDetail = MutableLiveData<Fiction>()

    init {
        Injector.fictionComponent.inject(this)
    }

    suspend fun addFictionToRepository(fiction: Fiction) = repository.add(fiction)

    suspend fun getBookshelfCategories(): List<String>? = repository.getCategories()

    suspend fun setBookshelfList(categoryName: String?) =
        fictionBookshelfList.postValue(repository.getFictionsByCategory(categoryName) as MutableList<Fiction>?)
}