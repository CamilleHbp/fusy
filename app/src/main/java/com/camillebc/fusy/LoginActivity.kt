package com.camillebc.fusy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

enum class Host {
    FANFICTION, ROYALROAD, DEFAULT
}

class LoginActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() // Children of supervisor job can fail independently

    @Inject lateinit var fictionHost: FictionHostInterface
    private var host = Host.ROYALROAD

    init {
        Injector.fictionComponent.inject(this)
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
        val login = login_login.text.toString()
        val password = login_password.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        when (host) {
            Host.ROYALROAD -> this.launch {  loginRoyalroad(login, password) }
            Host.FANFICTION -> loginFanfiction(login, password)
            else -> Toast.makeText(this, "Please, select a provider.", Toast.LENGTH_SHORT).show()
        }
    }

    fun switchHost(v: View) {
        when (v.tag.toString()) {
            "fanfiction" -> {
                login_logoHost.setImageDrawable(resources.getDrawable(R.drawable.banner_fanfiction, this.theme))
                host = Host.FANFICTION
            }
            "royalroad" -> {
                login_logoHost.setImageDrawable(resources.getDrawable(R.drawable.banner_royalroad, this.theme))
                host = Host.ROYALROAD
            }
            else -> {
                login_logoHost.setImageResource(R.color.mtrl_btn_transparent_bg_color)
                host = Host.DEFAULT
            }
        }
    }

    private fun launchAccountActivity() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    private fun loginFanfiction(login: String, password: String) {
        Toast.makeText(this, "TO IMPLEMENT", Toast.LENGTH_SHORT).show()
    }

    private suspend fun loginRoyalroad(login: String, password: String) {
        // "context" must be an Activity, Service or Application object from your app.
        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val py = Python.getInstance()
        val pyLogin = py.getModule("login")
        pyLogin.callAttr("login", login, password)
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
