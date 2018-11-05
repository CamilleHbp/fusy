package com.camillebc.fusy.network

import APP_TAG
import android.util.Log
import com.camillebc.fusy.interfaces.ApiInterface
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

private const val BASE_URL = "https://www.royalroad.com/"
private const val RETURN_URL = "https://www.royalroad.com/home"
private const val TAG = APP_TAG + "RoyalroadService"

class RoyalroadService: ApiInterface {
    override var cookieManager = CookieManager().apply { setCookiePolicy(CookiePolicy.ACCEPT_ALL) }

    private val networkInterface: RoyalroadInterface

    init {
        CookieHandler.setDefault(cookieManager)
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(logging)
            cookieJar(JavaNetCookieJar(cookieManager))
        }
        val retrofit = Retrofit.Builder().apply {
//            addConverterFactory(GsonConverterFactory.create(Gson()))
            addConverterFactory(ScalarsConverterFactory.create())
            baseUrl(BASE_URL)
            client(httpClient.build())
        }.build()
        networkInterface = retrofit.create(RoyalroadInterface::class.java)
    }

    override fun login(username: String, password: String) {
        val call = networkInterface.login(RETURN_URL, username, password, false)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.w(TAG, "Body: " + response.body())
                Log.w(TAG, "Raw: " + response.raw())
            }
        })
    }
 }