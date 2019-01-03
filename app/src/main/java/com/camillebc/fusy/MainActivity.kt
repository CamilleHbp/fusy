package com.camillebc.fusy

import APP_TAG
import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.camillebc.fusy.R.id.editText_login
import com.camillebc.fusy.R.id.editText_password
import com.camillebc.fusy.di.AppComponent
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.RoyalroadViewModel
import com.camillebc.fusy.data.RoyalroadViewModel.favoriteList
import com.camillebc.fusy.di.DaggerAppComponent
import com.camillebc.fusy.di.modules.ContextModule
import com.camillebc.fusy.network.RoyalroadProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity() {
    private val royalRoadApi = RoyalroadProvider()
    private lateinit var appComponent: AppComponent
    @Inject lateinit var app: Context
//    private lateinit var royalroadViewModel: RoyalroadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DaggerFun
        appComponent = DaggerAppComponent.builder().contextModule(ContextModule(this)).build()
        appComponent.inject(this)
        Toast.makeText(this, "AppContext: $app", Toast.LENGTH_SHORT).show()


        setContentView(R.layout.activity_main)
//        royalroadViewModel = ViewModelProviders.of(this).get(RoyalroadViewModel::class.java)
        val connectionObserver = Observer<Boolean> {
            if (it!!) {
                Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                // TODO("Implement Database") // The favorites will be initialized when creating the database in the future
                 val favouritesJob= CoroutineScope(Dispatchers.IO).launch {
                     val favourites = royalRoadApi.getFavouritesOrNull()

                     withContext(Dispatchers.Default) {
                         RoyalroadViewModel.favoriteList.postValue(favourites)
                 }
                }
            } else {
                Toast.makeText(this, "Login failed: check your login/password.", Toast.LENGTH_SHORT).show()
            }
        }
        RoyalroadViewModel.isConnected.observe(this, connectionObserver)
        val favoriteObserver = Observer<List<Fiction>> {
            launchAccountActivity()
        }
        RoyalroadViewModel.favoriteList.observe(this, favoriteObserver)
    }

    fun connect(v: View) {
        val login = editText_login.text.toString()
        val password = editText_password.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        royalRoadApi.login(login, password, RoyalroadViewModel.isConnected)
    }

    private fun launchAccountActivity() {
        RoyalroadViewModel.favoriteList.value!!.forEach {
            Log.i(TAG, "Result title: ${it.title}")
            Log.i(TAG, "Result description: ${it.description}")
        }
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    @Deprecated("Displays the cookie's values for debugging purposes")
    fun displayCookie(v: View) {
        RoyalroadViewModel.cookieManager.postValue(
            "Domain: ${royalRoadApi.cookieManager.cookieStore.cookies[0].domain}\n"
                    + "Name: ${royalRoadApi.cookieManager.cookieStore.cookies[0].name}\n"
                    + "Value: ${royalRoadApi.cookieManager.cookieStore.cookies[0].value}\n"
        )
    }
}
