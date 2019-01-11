package com.camillebc.fusy.utilities

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}