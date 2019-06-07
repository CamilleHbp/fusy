package com.camillebc.fusy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.ui.AccountFragment
import com.camillebc.fusy.ui.FirstLaunchFragment
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import com.camillebc.fusy.utilities.isFirstLaunch
import javax.inject.Inject

private const val TAG = APP_TAG + "MainActivity"

class MainActivity : AppCompatActivity(), AccountFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

        if (savedInstanceState != null) { // Fragment will be added again if the Activity already has one stored
            val fragment = if (this.application.isFirstLaunch()) FirstLaunchFragment() else AccountFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_main, fragment).commit()
        }
    }
}
