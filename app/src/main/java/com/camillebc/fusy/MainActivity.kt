package com.camillebc.fusy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import javax.inject.Inject

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity() {
    @Inject lateinit var hardwareStatusManager: HardwareStatusManager

    init {
        Injector.fictionComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val status = hardwareStatusManager.getConnectivityStatus().name
        val battery = hardwareStatusManager.getBatteryStatus().name

        Toast.makeText(this,
            "Connectivity status: $status\nBattery status: $battery", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
