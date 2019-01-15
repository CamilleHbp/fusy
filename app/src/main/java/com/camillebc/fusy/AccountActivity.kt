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
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionRepository
import com.camillebc.fusy.data.FictionViewModel
import com.camillebc.fusy.data.ROYALROAD
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FictionDetailFragment
import com.camillebc.fusy.fragments.FictionListFragment
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.addFragment
import com.camillebc.fusy.utilities.replaceFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = APP_TAG + "AccountActivity"
private const val ACCOUNT_BACKSTACK = "account_backstack"

class AccountActivity : AppCompatActivity(), FictionListFragment.OnListFragmentInteractionListener {
    @Inject lateinit var repository: FictionRepository
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

        GlobalScope.launch(Dispatchers.IO) {
            val favouriteFictionList = (repository.getFavourites(ROYALROAD)) as MutableList

            withContext(Dispatchers.Default) {
                fictionViewModel.fictionList.postValue(favouriteFictionList)
            }
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
            GlobalScope.launch(Dispatchers.IO) {
                fictionViewModel.fiction.postValue(repository.getFiction(item.id, ROYALROAD))
            }
            val detailFragment = FictionDetailFragment()
            replaceFragment(detailFragment, R.id.account_fragment, true, ACCOUNT_BACKSTACK)
        }
    }

    fun launchLibrary(v: View) {
        val intent = Intent(this, LibraryActivity::class.java)
        startActivity(intent)
    }
}
