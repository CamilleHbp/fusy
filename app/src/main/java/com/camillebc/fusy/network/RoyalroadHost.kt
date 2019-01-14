package com.camillebc.fusy.network

import android.util.Log
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
// COVER IMAGE
private const val COVER_IMAGE_QUERY = "img[id~=cover]"
// FAVOURITE QUERIES
private const val FAVOURITE_ITEM_QUERY = "div.fiction-list-item"             // parent element
private const val FAVOURITE_AUTHOR_QUERY = "div.fiction-info > span.author"
private const val FAVOURITE_AUTHOR_URL_QUERY = "$FAVOURITE_AUTHOR_QUERY > a"
private const val FAVOURITE_DESCRIPTION_QUERY = "div.description"
private const val FAVOURITE_NAME_QUERY = "h2.fiction-title"
private const val FAVOURITE_URL_QUERY = "$FAVOURITE_NAME_QUERY > a"
// SEARCH QUERIES
private const val SEARCH_ITEM_QUERY = "li.search-item"                      // parent element
private const val SEARCH_AUTHOR_QUERY = "div.fiction-info > span.author"
private const val SEARCH_DESCRIPTION_QUERY = "div.fiction-description"
private const val SEARCH_NAME_QUERY = "h2"
private const val SEARCH_NO_RESULT_QUERY = "h4"
private const val SEARCH_URL_QUERY = "$SEARCH_NAME_QUERY > a"
// TAG QUERIES
private const val TAGS_QUERY = "span.tags > span.label"                      // parent element
// FICTION QUERIES
private const val FICTION_AUTHOR_QUERY = "h4[property=\"author\"] > span[property=\"name\"]"
private const val FICTION_AUTHOR_URL_QUERY = "$FICTION_AUTHOR_QUERY > a"
private const val FICTION_DESCRIPTION_QUERY = "div[property=\"description\"]"
private const val FICTION_NAME_QUERY = "h1[property=\"name\"]"
// ID INDEXES
private const val FICTION_ID_INDEX = 2
private const val AUTHOR_ID_INDEX = 2

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
        val response = networkInterface.getFavourites().await()
        val fictionList = mutableListOf<Fiction>()
        Log.i(TAG, "GetFavourites")

        if (response.isSuccessful) {
            val doc = Jsoup.parse(response.body()?.string())
            val item = doc.select(FAVOURITE_ITEM_QUERY)
            Log.i(TAG, "$item")
            item.forEach { element ->
                val name = element.select(FAVOURITE_NAME_QUERY).text()
                val author = element.select(FAVOURITE_AUTHOR_QUERY).text()
                val authorId = element.select(FAVOURITE_AUTHOR_URL_QUERY).attr("href")
                    .split("/")[AUTHOR_ID_INDEX].toLong()
                val hostId = element.select(FAVOURITE_URL_QUERY).attr("href")
                    .split("/")[FICTION_ID_INDEX].toLong()
                val description = StringBuilder().also {
                    element.select(FAVOURITE_DESCRIPTION_QUERY).forEach { p ->
                        it.appendln(p.text())
                    }
                }.toString()
                var imageUrl = element.select(COVER_IMAGE_QUERY).first().absUrl("src")
                // DEBUG: Logging
                Log.i(TAG, "Name: $name")
                Log.i(TAG, "Host ID: $hostId")
                Log.i(TAG, "Author: $author")
                Log.i(TAG, "Author ID: $authorId")
                Log.i(TAG, "Description: $description")
                Log.i(TAG, "Image URL: $imageUrl")
                fictionList.add(Fiction(
                    name = name,
                    host = HOST,
                    hostId = hostId,
                    author = author,
                    authorId = authorId,
                    description = description,
                    favourite = false,
                    imageUrl = imageUrl
                ))
            }
        }
        return fictionList
    }

    override suspend fun getFiction(hostId: Long): Fiction? {
        val response = networkInterface.getFiction(hostId).await()
        var fiction: Fiction? = null
        Log.i(TAG, "[getFiction]")

        if (response.isSuccessful) {
            val doc = Jsoup.parse(response.body()?.string())
            val name = doc.select(FICTION_NAME_QUERY).text()
            val author= doc.select(FICTION_AUTHOR_QUERY).text()
            val authorId = doc.select(FICTION_AUTHOR_URL_QUERY).attr("href")
                .split("/")[AUTHOR_ID_INDEX].toLong()
            val description = StringBuilder().also {
                doc.select(FICTION_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                }
            }.toString()
            var imageUrl = doc.select(COVER_IMAGE_QUERY).first().absUrl("src")
            // DEBUG: Logging
            Log.i(TAG, "Name: $name")
            Log.i(TAG, "Host ID: $hostId")
            Log.i(TAG, "Author: $author")
            Log.i(TAG, "Author ID: $authorId")
            Log.i(TAG, "Description: $description")
            Log.i(TAG, "Image URL: $imageUrl")
            fiction = Fiction(
                name = name,
                host = HOST,
                hostId = hostId,
                author = author,
                authorId = authorId,
                description = description,
                favourite = false,
                imageUrl = imageUrl
            )
        }
        return fiction
    }

    override suspend fun getFictionTags(hostId: Long): List<Tag> {
        val response = networkInterface.getFiction(hostId).await()
        val tags = mutableListOf<Tag>()
        Log.i(TAG, "[getFictionTags]")

        if (response.isSuccessful) {
            val doc = Jsoup.parse(response.body()?.string())
            val tagElements = doc.select(TAGS_QUERY)
            tagElements.forEach {
                Log.i(TAG, "Tag: ${it.text()}")
                tags.add(Tag(it.text()))
            }
        }
        return tags
    }

    override suspend fun search(query: String): List<Fiction> {
        val response = networkInterface.search(query).await()
        val fictionList = mutableListOf<Fiction>()
        Log.i(TAG, "[search]")

        if (response.isSuccessful) {
            val doc = Jsoup.parse(response.body()?.string())
            val item = doc.select(SEARCH_ITEM_QUERY)
            item.forEach { element ->
                // Check if search returned any value
                val noResults = element.select(SEARCH_NO_RESULT_QUERY)
                if (noResults.text().contains("No results")) return emptyList()

                val name = element.select(SEARCH_NAME_QUERY).text()
                val author = element.select(SEARCH_AUTHOR_QUERY).text()
                val hostId = element.select(SEARCH_URL_QUERY).attr("href")
                    .split("/")[FICTION_ID_INDEX].toLong()
                val description = StringBuilder().also {
                    element.select(SEARCH_DESCRIPTION_QUERY).forEach { p -> it.appendln(p.text())
                    }
                }.toString()
                var imageUrl = element.select(COVER_IMAGE_QUERY).first().absUrl("src")
                // DEBUG: Logging
                Log.i(TAG, "Name: $name")
                Log.i(TAG, "Host ID: $hostId")
                Log.i(TAG, "Author: $author")
                Log.i(TAG, "Description: $description")
                Log.i(TAG, "Image URL: $imageUrl")
                fictionList.add(Fiction(
                    name = name,
                    host = HOST,
                    hostId = hostId,
                    author = author,
                    description = description,
                    favourite = false,
                    imageUrl = imageUrl
                ))
            }
        }
        return fictionList
    }

    suspend fun getAllTags(): List<Tag>{
        TODO("implement")
    }
}