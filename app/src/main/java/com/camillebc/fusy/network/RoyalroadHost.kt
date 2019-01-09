package com.camillebc.fusy.network

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.R
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.APP_TAG
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
import java.io.File
import java.io.IOException
import java.lang.Error
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://www.royalroad.com/"
private const val HOST = "royalroad"
private const val RETURN_URL = "https://www.royalroad.com/home"
private const val TAG = APP_TAG + "RoyalroadHost"
// JSOUP CSS QUERIES
private const val FAVORITE_IMAGE_QUERY = "img[id~=cover]"
private const val FAVORITE_ITEM_QUERY = "div.fiction-list-item"
private const val FAVORITE_TITLE_QUERY = "h2.fiction-title"
private const val FAVORITE_DESCRIPTION_QUERY = "div.description > div.hidden-content > p"
private const val SEARCH_IMAGE_QUERY = "img[id~=cover]"
private const val SEARCH_ITEM_QUERY = "li.search-item"
private const val SEARCH_TITLE_QUERY = "div.search-content > h2"
private const val SEARCH_DESCRIPTION_QUERY = "div.fiction-description > p"
private const val PLACEHOLDER_URL = "Content/Images/rr-placeholder.jpg"


@Singleton
class RoyalroadHost @Inject constructor(): FictionHostInterface {

    private val networkInterface: RoyalroadInterface
    private val cookieManager: CookieManager = CookieManager().apply { setCookiePolicy(CookiePolicy.ACCEPT_ALL) }

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
        Log.i(TAG, "Init")
    }

    override suspend fun login(username: String, password: String): Boolean {
        try {
            val response = networkInterface.login(RETURN_URL, username, password, false).await()
            if (response.isSuccessful) {
                Log.i(TAG, response.raw().request().url().toString())
                if (response.raw().request().url().toString().contains("loginsuccess")) {
                    Log.i(TAG, "Login successful!")
                    return true
                }
                Log.i(TAG, "Invalid login/password!")
                return false
            }
            Log.e(TAG, response.message())
            return false
        } catch (e: IOException) {
            Log.e(TAG, "Server non-responsive")
            return false
        }
    }

    override suspend fun getFavourites(): List<Fiction> {
        val response = networkInterface.getFavorites().await()

        if (response.isSuccessful) {
            Log.i(TAG, "GetFavorites")
            Log.i(TAG, "Response: $response")
            val mutableList = mutableListOf<Fiction>()
            val doc = Jsoup.parse(response.body()?.string())
            val item = doc.select(FAVORITE_ITEM_QUERY)
            Log.i(TAG, "${item.toString()}")
            item.forEachIndexed { index, element ->
                val title = element.select(FAVORITE_TITLE_QUERY).text()
                val description = StringBuilder().also {
                    element.select(FAVORITE_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                    }
                }.toString()
                val imageUrl = element.select(FAVORITE_IMAGE_QUERY).first().absUrl("src")
                Log.i(TAG, "Image url: $imageUrl")

                val fictionData = Fiction(title, imageUrl, description, true, HOST)
                mutableList.add(index, fictionData)
            }
            return mutableList.toList()
        }
        return listOf()
    }

    override suspend fun search(query: String): List<Fiction> {
        val response = networkInterface.search(query).await()

        if (response.isSuccessful) {
            Log.i(TAG, "Search")
            Log.i(TAG, "Response Body: ${response.body()}")
            val mutableList = mutableListOf<Fiction>()
            val doc = Jsoup.parse(response.body()?.string())
            Log.i(TAG, "DOC: \n$doc")
            val item = doc.select(SEARCH_ITEM_QUERY)
            Log.i(TAG, "${item.toString()}")
            item.forEachIndexed { index, element ->
                val title = element.select(SEARCH_TITLE_QUERY).text()
                val description = StringBuilder().also {
                    element.select(SEARCH_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                    }
                }.toString()
                var imageUrl = element.select(SEARCH_IMAGE_QUERY).first().absUrl("src")
                val fictionData = Fiction(title, imageUrl, description, true, HOST)
                mutableList.add(index, fictionData)
            }
            return mutableList.toList()
        }
        return listOf()
    }

    override fun updateReading(readingList: MutableLiveData<List<Fiction>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}