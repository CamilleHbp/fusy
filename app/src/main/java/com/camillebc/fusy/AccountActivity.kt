package com.camillebc.fusy

import APP_TAG
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.camillebc.fusy.data.FictionData
import com.camillebc.fusy.fragments.FavoriteFragment
import com.camillebc.fusy.fragments.dummy.DummyContent
import com.camillebc.fusy.utilities.addFragment

private const val TAG = APP_TAG + "AccountActivity"

class AccountActivity : AppCompatActivity(), FavoriteFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: FictionData?) {
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
    }
}
