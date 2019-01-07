package com.camillebc.fusy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.camillebc.fusy.data.RoyalroadViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() // Children of supervisor job can fail independently

    @Inject lateinit var hardwareStatusManager: HardwareStatusManager
    @Inject lateinit var fictionHost: FictionHostInterface

    init {
        Injector.getFictionComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val status = hardwareStatusManager.getConnectivityStatus().name
        val battery = hardwareStatusManager.getBatteryStatus().name

        Toast.makeText(this,
            "Connectivity status: $status\nBattery status: $battery", Toast.LENGTH_SHORT).show()

        val connectionObserver = Observer<Boolean> {
            if (it!!) {
                launchAccountActivity()
            }
        }
        RoyalroadViewModel.isConnected.observe(this, connectionObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
//         Cancel all running coroutines when destroying the activity
        coroutineContext[Job]!!.cancel()
    }

    suspend fun connect(login: String, password: String) {
        this.async {
            val loggedStatus = withContext(Dispatchers.IO) {
                fictionHost.login(login, password)
            }
            if (loggedStatus) Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this@MainActivity, "Offline", Toast.LENGTH_SHORT).show()
        }.await()
        launchAccountActivity()
    }

    fun connectOnClick(v: View) {
        val login = editText_login.text.toString()
        val password = editText_password.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        this.launch {  connect(login, password) }
    }

    private fun launchAccountActivity() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }
}
