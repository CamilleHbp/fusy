package com.camillebc.fusy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.camillebc.fusy.data.FictionRepository
import com.camillebc.fusy.data.RoyalroadViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity() {
    @Inject lateinit var hardwareStatusManager: HardwareStatusManager
    @Inject lateinit var repository: FictionRepository

    init {
        Injector.getFictionComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val status = hardwareStatusManager.getConnectivityStatus().toString()
        val type = hardwareStatusManager.getConnectivityType().name
        val battery = hardwareStatusManager.getBatteryStatus().name

        Toast.makeText(this,
            "Connectivity status: $status\nConnectivity type: $type\nBattery status: $battery",
            Toast.LENGTH_SHORT).show()

        val connectionObserver = Observer<Boolean> {
            if (it!!) {
                launchAccountActivity()
            }
        }
        RoyalroadViewModel.isConnected.observe(this, connectionObserver)
    }

    fun connect(v: View) {
        val login = editText_login.text.toString()
        val password = editText_password.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        repository.login(login, password, RoyalroadViewModel.isConnected)
    }

    private fun launchAccountActivity() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }
}
