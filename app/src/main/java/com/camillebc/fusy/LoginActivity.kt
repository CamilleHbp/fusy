package com.camillebc.fusy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.interfaces.FictionHostInterface
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() // Children of supervisor job can fail independently

    @Inject lateinit var fictionHost: FictionHostInterface

    init {
        Injector.getFictionComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext[Job]!!.cancel() // Cancel all running coroutines when destroying the activity
    }

    fun login(v: View) {
        val login = editText_login.text.toString()
        val password = editText_password.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        this.launch {  loginRoyalroad(login, password) }
    }

    private fun launchAccountActivity() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    private suspend fun loginRoyalroad(login: String, password: String) {
        this.async {
            val loggedStatus = withContext(Dispatchers.IO) {
                fictionHost.login(login, password)
            }
            if (loggedStatus) Toast.makeText(this@LoginActivity, "Connected", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this@LoginActivity, "Offline", Toast.LENGTH_SHORT).show()
        }.await()
        launchAccountActivity()
    }
}
