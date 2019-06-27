package com.camillebc.fusy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.camillebc.fusy.account.model.Account
import com.camillebc.fusy.account.view.BookshelfFragment
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.utilities.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import me.camillebc.fictionproviderapi.ApiProvider
import me.camillebc.fictionproviderapi.FictionProvider
import me.camillebc.utilities.HardwareStatusManager
import javax.inject.Inject


private const val TAG_FIRST_LAUNCH = "LoginFragment"

class MainActivity : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.IO),
    BookshelfFragment.OnFragmentInteractionListener {

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

        val royalRoadApi = ApiProvider.getApi(FictionProvider.ROYALROAD)
        launch {
            royalRoadApi.getProviderTags().also {
                it.forEach { tag ->
                    logi(tag)
                }
            }
        }
        launch {
            val fictions = royalRoadApi.search("test")

            fictions.consumeEach {
                logi("Search result: ${it.name}")
            }
        }
    }

//    override fun onBackPressed() {
//        // Disable back key on first launch: we need to choose an account type
//        val firstLaunchFragment = supportFragmentManager.findFragmentByTag(TAG_FIRST_LAUNCH)
//        if (firstLaunchFragment != null) {
//            Toast.makeText(this, "Please choose your account type.", Toast.LENGTH_SHORT).show()
//        } else {
//            super.onBackPressed()
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            data?.let { Account.setGoogleAccount(it) }
            Toast.makeText(this, "Signed in as " + Account.getName(), Toast.LENGTH_SHORT).show()
            val accountFragment = BookshelfFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_activityMain_navHost, accountFragment).commit()
            supportFragmentManager.popBackStack(TAG_FIRST_LAUNCH, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onBookshelfFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

