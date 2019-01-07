package com.camillebc.fusy.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FictionViewModel: ViewModel() {
    val favoriteList = MutableLiveData<List<Fiction>>().apply {  value = listOf() }
}