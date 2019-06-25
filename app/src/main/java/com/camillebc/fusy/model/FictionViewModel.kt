package com.camillebc.fusy.model

import me.camillebc.fictionhostapi.Fiction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FictionViewModel: ViewModel() {
    val fictionList =  MutableLiveData<MutableList<Fiction>>(mutableListOf())
    val fiction = MutableLiveData<Fiction>()
}