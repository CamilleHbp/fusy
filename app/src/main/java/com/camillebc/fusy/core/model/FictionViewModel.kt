package com.camillebc.fusy.core.model

import me.camillebc.fictionproviderapi.FictionMetadata
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FictionViewModel: ViewModel() {
    val fictionSearchList =  MutableLiveData<MutableList<FictionMetadata>>(mutableListOf())
    val fictionDetail = MutableLiveData<FictionMetadata>()
}