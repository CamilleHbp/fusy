package com.camillebc.fusy

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.camillebc.fusy.bookshelf.view.FictionDetailFragment
import com.camillebc.fusy.core.APP_PREF
import com.camillebc.fusy.core.di.Injector
import com.camillebc.fusy.core.model.Fiction
import com.camillebc.fusy.core.model.FictionViewModel
import com.camillebc.fusy.reader.ReaderFragment
import com.camillebc.fusy.searchengine.view.SearchFragment
import com.camillebc.fusy.utilities.logi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.camillebc.utilities.HardwareStatusManager
import me.camillebc.utilities.extensions.setupWithNavController
import javax.inject.Inject

private const val TAG_FIRST_LAUNCH = "LoginFragment"

class MainActivity :
    AppCompatActivity(),
    CoroutineScope by CoroutineScope(Dispatchers.IO),
    FictionDetailFragment.OnDetailFragmentInteractionListener,
//    NavigationView.OnNavigationItemSelectedListener,
    ReaderFragment.OnReaderFragmentInteractionListener,
    SearchFragment.OnSearchFragmentInteractionListener {

    @Inject
    lateinit var hardwareStatusManager: HardwareStatusManager
    private lateinit var fictionViewModel: FictionViewModel
    private var currentNavController: LiveData<NavController>? = null
    private var navHostFragmentMap: Map<Int, NavHostFragment>? = null

    init {
        Injector.fictionComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fictionViewModel = ViewModelProviders.of(this).get(FictionViewModel::class.java)
        setContentView(R.layout.activity_main)
        val status = hardwareStatusManager.getConnectivityStatus().name
        val battery = hardwareStatusManager.getBatteryStatus().name

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        Toast.makeText(
            this,
            "Connectivity status: $status\nBattery status: $battery", Toast.LENGTH_SHORT
        ).show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            data?.let { Account.setGoogleAccount(it) }
//            Toast.makeText(this, "Signed in as " + Account.getName(), Toast.LENGTH_SHORT).show()
//            val accountFragment = BookshelfFragment()
//            replaceFragment(R.id.constraintLayout_activityMain_navHost, accountFragment)
//            supportFragmentManager.popBackStack(TAG_FIRST_LAUNCH, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        }
//    }

    override fun onReaderFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSearchFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAdd(item: Fiction) {
        launch { fictionViewModel.addFictionToRepository(item) }
        Toast.makeText(this, "${item.name} added to the library.", Toast.LENGTH_SHORT).show()
    }

    override fun onRead(item: Fiction) {
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

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val containerId = R.id.constraintLayout_activityMain_navHost
//        val navHostFragment = when (item.itemId) {
//            R.id.library -> navHostFragmentMap!![R.navigation.library] as Fragment
//            R.id.reader -> navHostFragmentMap!![R.navigation.reader] as Fragment
//            R.id.search_engine -> navHostFragmentMap!![R.navigation.search_engine] as Fragment
//            else -> return false
//        }
//        supportFragmentManager.beginTransaction()
//            .replace(containerId, navHostFragment, "library")
//            .setPrimaryNavigationFragment(navHostFragment)
//            .commitNow()
//        return true
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_activityMain)

        val navGraphIds =
            listOf(R.navigation.reader, R.navigation.library, R.navigation.search_engine)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.constraintLayout_activityMain_navHost,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}

