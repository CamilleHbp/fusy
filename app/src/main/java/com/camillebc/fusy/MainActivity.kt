package com.camillebc.fusy

import APP_TAG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.camillebc.fusy.data.FictionData
import com.camillebc.fusy.data.RoyalroadViewModel
import com.camillebc.fusy.network.RoyalroadService
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity() {
    private val royalRoadApi = RoyalroadService()
//    private lateinit var royalroadViewModel: RoyalroadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        royalroadViewModel = ViewModelProviders.of(this).get(RoyalroadViewModel::class.java)
        val connectionObserver = Observer<Boolean> {
            if (it!!) {
                Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                // TODO("Implement Database") // The favorites will be initialized when creating the database in the future
                royalRoadApi.getFavorites(RoyalroadViewModel.favoriteList)
            } else {
                Toast.makeText(this, "Login failed: check your login/password.", Toast.LENGTH_SHORT).show()
            }
        }
        RoyalroadViewModel.isConnected.observe(this, connectionObserver)
        val favoriteObserver = Observer<List<FictionData>> {
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
