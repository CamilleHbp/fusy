package com.camillebc.fusy

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camillebc.fusy.account.view.AccountFragment
import com.camillebc.fusy.account.view.FirstLaunchFragment
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.HardwareStatusManager
import com.camillebc.fusy.utilities.isFirstLaunch
import javax.inject.Inject

private const val TAG = APP_TAG + "MainActivity"
private const val TAG_FIRST_LAUNCH = "FirstLaunchFragment"

class MainActivity : AppCompatActivity(), AccountFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var hardwareStatusManager: HardwareStatusManager

    init {
        Injector.fictionComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val status = hardwareStatusManager.getConnectivityStatus().name
        val battery = hardwareStatusManager.getBatteryStatus().name

        Toast.makeText(
            this,
            "Connectivity status: $status\nBattery status: $battery", Toast.LENGTH_SHORT
        ).show()

        if (savedInstanceState == null) { // Fragment will be added again if the Activity already has one stored
            val fragment = if (this.application.isFirstLaunch()) FirstLaunchFragment() else AccountFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_main, fragment, TAG_FIRST_LAUNCH).commit()
        }
    }

    override fun onBackPressed() {
        // Disable back key on first launch: we need to choose an account type
        val firstLaunchFragment = supportFragmentManager.findFragmentByTag(TAG_FIRST_LAUNCH)
        if (firstLaunchFragment != null) {
            Toast.makeText(this, "Please choose your account type.", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}
