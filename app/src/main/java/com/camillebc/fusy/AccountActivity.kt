package com.camillebc.fusy

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.model.Fiction
import com.camillebc.fusy.model.FictionViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FictionDetailFragment
import com.camillebc.fusy.fragments.FictionListFragment
import com.camillebc.fusy.utilities.APP_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.camillebc.utilities.extensions.addFragment
import me.camillebc.utilities.extensions.replaceFragment
import javax.inject.Inject

private const val TAG = APP_TAG + "AccountActivity"

class AccountActivity : AppCompatActivity(), FictionListFragment.OnListFragmentInteractionListener {
    private lateinit var fictionViewModel: FictionViewModel

    init {
        Injector.fictionComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        fictionViewModel = ViewModelProviders.of(this).get(FictionViewModel::class.java)
        val favoriteFragment = FictionListFragment()
        addFragment(favoriteFragment, R.id.account_fragment)

        val favouriteFictionList = mutableListOf<Fiction>()
        GlobalScope.launch(Dispatchers.IO) {
//            favouriteFictionList.addAll(host.getFavourites())
//            withContext(Dispatchers.Default) {
//                fictionViewModel.fictionList.postValue(favouriteFictionList)
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.menu_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onListFragmentInteraction(item: Fiction?) {
        if (item != null) {
//            GlobalScope.launch(Dispatchers.IO) {
//                fictionViewModel.fiction.postValue(host.getFiction(item.hostId))
//            }
            val detailFragment = FictionDetailFragment()
            replaceFragment(detailFragment, R.id.account_fragment, true)
        }
    }

    fun launchLibrary(v: View) {
        val intent = Intent(this, LibraryActivity::class.java)
        startActivity(intent)
    }
}
