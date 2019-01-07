package com.camillebc.fusy

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionRepository
import com.camillebc.fusy.data.RoyalroadViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FavouriteFragment
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.addFragment
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = APP_TAG + "AccountActivity"

class AccountActivity : AppCompatActivity(), FavouriteFragment.OnListFragmentInteractionListener {
    @Inject lateinit var app: Context
    @Inject lateinit var repository: FictionRepository

    init {
        Injector.getFictionComponent().inject(this)
    }

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

        GlobalScope.launch(Dispatchers.IO) {
            val favourites = repository.getFavourites()

            withContext(Dispatchers.Default) {
                RoyalroadViewModel.favoriteList.postValue(favourites)
            }
        }
    }
}
