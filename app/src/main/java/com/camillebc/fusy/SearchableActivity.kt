package com.camillebc.fusy

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FictionDetailFragment
import com.camillebc.fusy.fragments.FictionListFragment
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.addFragment
import com.camillebc.fusy.utilities.replaceFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = APP_TAG + "SearchableActivity"

class SearchableActivity : AppCompatActivity(), FictionListFragment.OnListFragmentInteractionListener  {
    @Inject lateinit var host: FictionHostInterface
    private lateinit var fictionViewModel: FictionViewModel

    init {
        Injector.getFictionComponent().inject(this)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                search(query)
            }
        }

        fictionViewModel = ViewModelProviders.of(this).get(FictionViewModel::class.java)
        val favoriteFragment = FictionListFragment()
        addFragment(favoriteFragment, R.id.searchable_fragment_layout)
    }
   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.menu_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onListFragmentInteraction(item: Fiction?) {
        if (item != null) {
            GlobalScope.launch(Dispatchers.IO) {
                fictionViewModel.fiction.postValue(host.getFiction(item.hostId))
            }
            val detailFragment = FictionDetailFragment()
            replaceFragment(detailFragment, R.id.searchable_fragment_layout, true)
        }
    }

    private fun search(query: String) {
        val searchFictionList = mutableListOf<Fiction>()

        GlobalScope.launch(Dispatchers.IO) {
            searchFictionList.addAll(host.search(query))
            withContext(Dispatchers.Default) {
                fictionViewModel.fictionList.postValue(searchFictionList)
            }
        }
    }
}
