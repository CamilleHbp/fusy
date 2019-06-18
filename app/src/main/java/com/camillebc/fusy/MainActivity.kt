package com.camillebc.fusy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camillebc.fusy.account.model.Account
import com.camillebc.fusy.account.view.AccountFragment
import com.camillebc.fusy.account.view.FirstLaunchFragment
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.utilities.APP_PREF
import com.camillebc.fusy.utilities.RC_SIGN_IN
import com.camillebc.fusy.utilities.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.camillebc.fictionhostapi.royalroad.RoyalRoadApi
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Inject


private const val TAG_FIRST_LAUNCH = "FirstLaunchFragment"

class MainActivity : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.IO),
    AccountFragment.OnFragmentInteractionListener {

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
            val fragment = if (isFirstLaunch()) FirstLaunchFragment() else AccountFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_main, fragment, TAG_FIRST_LAUNCH).commit()
        }

        launch {
            RoyalRoadApi.getTags().also {
                it.forEach { tag ->
                    logi(tag)
                }
            }
        }
        launch {
            val fiction = RoyalRoadApi.getFiction("24709/defiance-of-the-fall")
            logi("Fiction Name: " + fiction.name)
            logi("Fiction Author: " + fiction.author)
            logi("Fiction Description:\n" + fiction.description)

            fiction.chapters.forEach { chapter ->
                logi(chapter.id)
            }
            RoyalRoadApi.getChapter(fiction.chapters[1])
            logi("Chapter title: " + fiction.chapters[1].title)
            fiction.chapters[1].content?.forEach {
                logi("Chapter html content: $it ")
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            data?.let { Account.setGoogleAccount(it) }
            Toast.makeText(this, "Signed in as " + Account.getName(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isFirstLaunch(): Boolean {
        val prefsName = APP_PREF
        val prefVersionCodeKey = "version_code"
        val inexistent = -1

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(prefVersionCodeKey, inexistent)

        // Update the shared preferences with the current version code
        prefs.edit().putInt(prefVersionCodeKey, currentVersionCode).apply()

        // Check for first run or upgrade
        // This is just a normal run
        return when {
            currentVersionCode == savedVersionCode -> false
            currentVersionCode == inexistent -> true // TODO This is a new install (or the user cleared the shared preferences)
            currentVersionCode > savedVersionCode -> true // TODO This is an upgrade
            else -> true
        }
    }
}

