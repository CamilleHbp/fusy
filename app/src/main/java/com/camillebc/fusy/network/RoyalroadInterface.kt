package com.camillebc.fusy.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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
    ): Deferred<Response<ResponseBody>>

    @GET("my/favorites")
    fun getFavorites(): Deferred<Response<ResponseBody>>

    @GET("fiction/{id}")
    fun getFiction(@Path("id") id: Long): Deferred<Response<ResponseBody>>

    @GET("fictions/search")
    fun search(@Query("keyword") keyword: String): Deferred<Response<ResponseBody>>
}
