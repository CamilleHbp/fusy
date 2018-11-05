package com.camillebc.fusy

import APP_TAG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.camillebc.fusy.data.NetworkViewModel
import com.camillebc.fusy.network.RoyalroadService
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity() {
    private val royalRoadApi = RoyalroadService()
    private lateinit var networkViewModel: NetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkViewModel = ViewModelProviders.of(this).get(NetworkViewModel::class.java)
        val observer = Observer<String>{
            textView_cookie.text = it
        }
        networkViewModel.cookieManager.observe(this, observer)
    }

    fun connect(v: View) {
        val login = editText_login.text.toString()
        val password = editText_password.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        royalRoadApi.login(login, password)
    }

    fun displayCookie(v: View) {
        networkViewModel.cookieManager.postValue(
            "Domain: ${royalRoadApi.cookieManager.cookieStore.cookies[0].domain}\n"
                    + "Name: ${royalRoadApi.cookieManager.cookieStore.cookies[0].name}\n"
                    + "Value: ${royalRoadApi.cookieManager.cookieStore.cookies[0].value}\n"
        )
    }
}
