package com.camillebc.fusy.core.model

import me.camillebc.fictionproviderapi.FictionMetadata
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FictionViewModel: ViewModel() {
    val fictionList =  MutableLiveData<MutableList<FictionMetadata>>(mutableListOf())
    val fiction = MutableLiveData<FictionMetadata>()
}