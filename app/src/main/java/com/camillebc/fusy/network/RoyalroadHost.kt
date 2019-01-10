package com.camillebc.fusy.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.Tag
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
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
// FAVOURITE QUERIES
private const val FAVORITE_ITEM_QUERY = "div.fiction-list-item"             // parent element
private const val FAVORITE_DESCRIPTION_QUERY = "div.description > div.hidden-content > p"
private const val FAVORITE_IMAGE_QUERY = "img[id~=cover]"
private const val FAVORITE_TITLE_QUERY = "h2.fiction-name"
private const val FAVORITE_URL_QUERY = "h2.fiction-name > a"
// SEARCH QUERIES
private const val SEARCH_ITEM_QUERY = "li.search-item"                      // parent element
private const val SEARCH_DESCRIPTION_QUERY = "div.fiction-description"
private const val SEARCH_IMAGE_QUERY = "img[id~=cover]"
private const val SEARCH_TITLE_QUERY = "div.search-content > h2"
private const val SEARCH_URL_QUERY = "div.search-content > h2 > a"
// FICTION QUERIES
private const val TAGS_QUERY = "span.tags > span.label"                      // parent element
private const val FICTION_AUTHOR_QUERY = "h4[property=\"author\"] > span[property=\"name\"]"
private const val FICTION_NAME_QUERY = "h1[property=\"name\"]"

private val ID_INDEX = 2

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
                val url = element.select(FAVORITE_URL_QUERY).attr("href")
                val description = StringBuilder().also {
                    element.select(FAVORITE_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                    }
                }.toString()
                val imageUrl = element.select(FAVORITE_IMAGE_QUERY).first().absUrl("src")
                Log.i(TAG, "Image url: $imageUrl")

                val fictionData = Fiction(
                    name = title,
                    imageUrl = imageUrl,
                    hostId = 0,
                    description = description,
                    favourite = true,
                    host = HOST
                )
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
                val hostId = element.select(SEARCH_URL_QUERY).attr("href").split("/")[2].toLong()
                val description = StringBuilder().also {
                    element.select(SEARCH_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                    }
                }.toString()
                var imageUrl = element.select(SEARCH_IMAGE_QUERY).first().absUrl("src")
                val fictionData = Fiction(
                    name = title,
                    hostId = hostId,
                    imageUrl = imageUrl,
                    description = description,
                    favourite = false,
                    host = HOST
                )
                mutableList.add(index, fictionData)
            }
            return mutableList.toList()
        }
        return listOf()
    }

    override suspend fun getFiction(hostId: Long): Fiction {
        val response = networkInterface.getFiction(hostId).await()

        if (response.isSuccessful) {
            Log.i(TAG, "[getFiction]")
            val doc = Jsoup.parse(response.body()?.string())
            val name = doc.select(FICTION_NAME_QUERY).text()
            Log.i(TAG, "Name: $name")
            val author = doc.select(FICTION_AUTHOR_QUERY).text()
            Log.i(TAG, "Author: $author")
//            val item = doc.select(SEARCH_ITEM_QUERY)
//            Log.i(TAG, "${item.toString()}")
//            item.forEachIndexed { index, element ->
//                val name = element.select(SEARCH_TITLE_QUERY).text()
//                val url = element.select(SEARCH_URL_QUERY).attr("href")
//                val description = StringBuilder().also {
//                    element.select(SEARCH_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
//                    }
//                }.toString()
//                var imageUrl = element.select(SEARCH_IMAGE_QUERY).first().absUrl("src")
//                val fictionData = Fiction(
//                    name = name,
//                    imageUrl = imageUrl,
//                    url = url,
//                    description = description,
//                    favourite = false,
//                    host = HOST
//                )
//                mutableList.add(index, fictionData)
        }
        return Fiction(
            name = "",
            host = "royalroad",
            hostId = 0
            )
    }

    override suspend fun getFictionTags(hostId: Long): List<Tag> {
        val response = networkInterface.getFiction(hostId).await()
        val tags = mutableListOf<Tag>()

        if (response.isSuccessful) {
            Log.i(TAG, "[getFictionTags]")
            val mutableList = mutableListOf<Fiction>()
            val doc = Jsoup.parse(response.body()?.string())
            val tagElements = doc.select(TAGS_QUERY)
            tagElements.forEach {
                Log.i(TAG, "Tag: ${it.text()}")
                tags.add(Tag(it.text()))
            }
        }
        return tags
    }

    suspend fun getAllTags(): List<Tag>{
        TODO("implement")
    }

    override fun updateReading(readingList: MutableLiveData<List<Fiction>>) {
        TODO("implement")
    }
}