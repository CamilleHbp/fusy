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
import com.camillebc.fusy.fragments.FavoriteFragment
import com.camillebc.fusy.utilities.addFragment
import javax.inject.Inject

private const val TAG = APP_TAG + "AccountActivity"

class AccountActivity : AppCompatActivity(), FavoriteFragment.OnListFragmentInteractionListener {
    private lateinit var fictionComponent: FictionComponent
    @Inject lateinit var app: Context
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

        val favoriteFragment = FavoriteFragment()
        addFragment(favoriteFragment, R.id.favorite_layout)

        fictionComponent = initDagger(this)
        fictionComponent.inject(this)
    }

    private fun initDagger(context: Context): FictionComponent =
        DaggerFictionComponent.builder().contextModule(ContextModule(this)).build()
}
