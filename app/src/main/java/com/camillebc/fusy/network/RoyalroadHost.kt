package com.camillebc.fusy.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://www.royalroad.com/"
private const val RETURN_URL = "https://www.royalroad.com/home"
private const val TAG = APP_TAG + "RoyalroadHost"
// JSOUP CSS QUERIES
private const val FAVORITE_IMAGE_QUERY = "img[id~=cover]"
private const val FAVORITE_ITEM_QUERY = "div.fiction-list-item"
private const val FAVORITE_TITLE_QUERY = "h2.fiction-title"
private const val FAVORITE_DESCRIPTION_QUERY = "div.description > div.hidden-content > p"


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
                    Log.i(TAG, "Login cookies: ${cookieManager.cookieStore.cookies}")
                    isLoggedIn.postValue(true)
                }
                else isLoggedIn.postValue(false)
            }
        })
    }

    override suspend fun getFavouritesOrNull(): List<Fiction>? {
        val response = networkInterface.getFavorites().await()

        Log.i(TAG, "Favourites Cookies: ${cookieManager.cookieStore.cookies}")

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

                val fictionData = Fiction(title, imageUrl, description, true, "royalroad")
                mutableList.add(index, fictionData)
            }
            return mutableList.toList()
        }
        return null
    }

    override fun updateReading(readingList: MutableLiveData<List<Fiction>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}