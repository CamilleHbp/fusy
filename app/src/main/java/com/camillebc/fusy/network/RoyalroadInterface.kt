package com.camillebc.fusy.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

private const val RETURN_URL_KEY = "returnUrl"
private const val USERNAME_KEY = "Username"
private const val PASSWORD_KEY = "Password"
private const val REMEMBER_KEY = "Password"

interface RoyalroadInterface {
    @FormUrlEncoded
    @POST("account/login")
    fun login(
        @Query(RETURN_URL_KEY) returnUrl: String,
        @Field(USERNAME_KEY) username: String,
        @Field(PASSWORD_KEY) password: String,
        @Field(REMEMBER_KEY) remember: Boolean
    ): Call<String>
}