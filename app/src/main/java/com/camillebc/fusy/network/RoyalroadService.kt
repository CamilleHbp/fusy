package com.camillebc.fusy.network

import APP_TAG
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.text.Html
import android.util.Log
import android.widget.Toast
import com.camillebc.fusy.interfaces.ApiInterface
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.StringBuilder
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.text.Typography.paragraph

private const val BASE_URL = "https://www.royalroad.com/"
private const val RETURN_URL = "https://www.royalroad.com/home"
private const val TAG = APP_TAG + "RoyalroadService"

class RoyalroadService: ApiInterface {
    val cookieManager = CookieManager().apply { setCookiePolicy(CookiePolicy.ACCEPT_ALL) }

    private val networkInterface: RoyalroadInterface

    init {
        CookieHandler.setDefault(cookieManager)
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(logging)
            cookieJar(JavaNetCookieJar(cookieManager))
        }
        val retrofit = Retrofit.Builder().apply {
            addConverterFactory(ScalarsConverterFactory.create())
            baseUrl(BASE_URL)
            client(httpClient.build())
        }.build()
        networkInterface = retrofit.create(RoyalroadInterface::class.java)
    }

    override fun login(username: String, password: String, activity: Activity) {
        val call = networkInterface.login(RETURN_URL, username, password, false)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) Toast.makeText(activity, "Login successful!", Toast.LENGTH_SHORT).show()
                else Toast.makeText(activity, "Login failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getFavorites(liveData: MutableLiveData<String>) {
        val call = networkInterface.getFavorites()
        call.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val stringBuilder = StringBuilder()
                val doc = Jsoup.parse(response.body()?.string())
                val item = doc.select("div.fiction-list-item")
                item.forEach { item ->
                    stringBuilder.append("Title: ${item.select("h2.fiction-title").text()}\n")
                    Log.i(TAG, "Title: ${item.select("h2.fiction-title").text()}")
                    stringBuilder.append("Description: ")
                    val description = item.select("div.description > div.hidden-content > p")
                    description.forEach { p ->
                        Log.i(TAG, "desc: ${p.text()}")
                        stringBuilder.append(p.text())
                        stringBuilder.append("\n")
                    }
                    stringBuilder.append("\n")
                    liveData.postValue(stringBuilder.toString())
                }
            }
        })
    }
}