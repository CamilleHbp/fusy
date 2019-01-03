package com.camillebc.fusy.network

import APP_TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.interfaces.FictionProviderInterface
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
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
private const val TAG = APP_TAG + "RoyalroadProvider"
// JSOUP CSS QUERIES
private const val FAVORITE_IMAGE_QUERY = "img[id~=cover]"
private const val FAVORITE_ITEM_QUERY = "div.fiction-list-item"
private const val FAVORITE_TITLE_QUERY = "h2.fiction-title"
private const val FAVORITE_DESCRIPTION_QUERY = "div.description > div.hidden-content > p"


class RoyalroadProvider: FictionProviderInterface {

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
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            addConverterFactory(ScalarsConverterFactory.create())
            baseUrl(BASE_URL)
            client(httpClient.build())
        }.build()
        networkInterface = retrofit.create(RoyalroadInterface::class.java)
    }

    override fun login(username: String, password: String, isLoggedIn: MutableLiveData<Boolean>) {
        val call = networkInterface.login(RETURN_URL, username, password, false)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, t.message)
                isLoggedIn.postValue(false)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "Login successful!")
                    isLoggedIn.postValue(true)
                }
                else isLoggedIn.postValue(false)
            }
        })
    }

    override suspend fun getFavouritesOrNull(): List<Fiction>? {
        val response = networkInterface.getFavorites().await()

        if (response.isSuccessful) {
            Log.i(TAG, "GetFavorites")
            val mutableList = mutableListOf<Fiction>()
            val doc = Jsoup.parse(response.body()?.string())
            val item = doc.select(FAVORITE_ITEM_QUERY)
            item.forEachIndexed { index, element ->
                val title = element.select(FAVORITE_TITLE_QUERY).text()
                val description = StringBuilder().also {
                    element.select(FAVORITE_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                    }
                }.toString()
                val imageUrl = element.select(FAVORITE_IMAGE_QUERY).first().absUrl("src")
                Log.i(TAG, "Image url: $imageUrl")

                val fictionData = Fiction(title, imageUrl, description, true, "royalroad")
                mutableList.add(index, fictionData)
            }
            return mutableList.toList()
        }
        return null
//        call.enqueue(object: Callback<ResponseBody> {
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e(TAG, t.message)
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                Log.i(TAG, "GetFavorites")
//                val mutableList = mutableListOf<Fiction>()
//                val doc = Jsoup.parse(response.body()?.string())
//                val item = doc.select(FAVORITE_ITEM_QUERY)
//                item.forEachIndexed { index, element ->
//                    val title = element.select(FAVORITE_TITLE_QUERY).text()
//                    val description = StringBuilder().also {
//                        element.select(FAVORITE_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
//                        }
//                    }.toString()
//                    val imageUrl = element.select(FAVORITE_IMAGE_QUERY).first().absUrl("src")
//                    Log.i(TAG, "Image url: $imageUrl")
//
//                    val fictionData = Fiction(title, imageUrl, description, true, "royalroad")
//                    mutableList.add(index, fictionData)
//                }
//                mutableList.toList()
//            }
//        })
    }

    override fun updateReading(readingList: MutableLiveData<List<Fiction>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}