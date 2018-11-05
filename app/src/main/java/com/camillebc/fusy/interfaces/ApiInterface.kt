package com.camillebc.fusy.interfaces

import java.net.CookieManager


interface ApiInterface {
    var cookieManager: CookieManager

    fun login(username: String, password: String)
}