package com.camillebc.fusy.interfaces

import android.app.Activity

interface ApiInterface {
    fun login(username: String, password: String, activity: Activity)
}