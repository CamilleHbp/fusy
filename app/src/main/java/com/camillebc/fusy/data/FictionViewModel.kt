package com.camillebc.fusy.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FictionViewModel: ViewModel() {
    val fictionList =  MutableLiveData<MutableList<Fiction>>().apply { value = mutableListOf() }
    val fiction = MutableLiveData<Fiction>()
}