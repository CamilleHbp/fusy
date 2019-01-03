package com.camillebc.fusy

import APP_TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionDatabase
import com.camillebc.fusy.di.DaggerFictionComponent
import com.camillebc.fusy.di.FictionComponent
import com.camillebc.fusy.di.modules.ContextModule
import com.camillebc.fusy.di.modules.FictionDatabaseModule
import com.camillebc.fusy.di.modules.HardwareStatusModule
import com.camillebc.fusy.fragments.FavouriteFragment
import com.camillebc.fusy.utilities.HardwareStatusManager
import com.camillebc.fusy.utilities.addFragment
import javax.inject.Inject

private const val TAG = APP_TAG + "AccountActivity"

class AccountActivity : AppCompatActivity(), FavouriteFragment.OnListFragmentInteractionListener {
    private lateinit var fictionComponent: FictionComponent
    @Inject lateinit var app: Context
    @Inject lateinit var hardwareStatusManager: HardwareStatusManager
    @Inject lateinit var fictionDatabase: FictionDatabase

    override fun onListFragmentInteraction(item: Fiction?) {
        if (item != null) {
            Log.i(TAG, item?.title)
            Toast.makeText(this, "Selected: ${item.title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val favoriteFragment = FavouriteFragment()
        addFragment(favoriteFragment, R.id.favorite_layout)

        fictionComponent = initDagger(this)
        fictionComponent.inject(this)
        val status = hardwareStatusManager.getConnectivityStatus().toString()
        val type = hardwareStatusManager.getConnectivityType().name
        val battery = hardwareStatusManager.getBatteryStatus().name

        Toast.makeText(this,
            "Connectivity status: $status\nConnectivity type: $type\nBattery status: $battery",
            Toast.LENGTH_SHORT).show()
    }

    private fun initDagger(context: Context): FictionComponent =
        DaggerFictionComponent.builder()
            .contextModule(ContextModule(this))
            .fictionDatabaseModule(FictionDatabaseModule())
            .hardwareStatusModule(HardwareStatusModule())
            .build()
}
