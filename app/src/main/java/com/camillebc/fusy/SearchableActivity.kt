package com.camillebc.fusy

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FictionDetailFragment
import com.camillebc.fusy.fragments.FictionListFragment
import com.camillebc.fusy.model.Fiction
import com.camillebc.fusy.model.FictionViewModel
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.camillebc.fictionhostapi.royalroad.RoyalRoadApi
import me.camillebc.utilities.extensions.addFragment
import me.camillebc.utilities.extensions.replaceFragment

private const val TAG = APP_TAG + "SearchableActivity"

class SearchableActivity : AppCompatActivity(), FictionListFragment.OnListFragmentInteractionListener,
    CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private lateinit var fictionViewModel: FictionViewModel

    init {
        Injector.fictionComponent.inject(this)
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
        addFragment(favoriteFragment, R.id.fragment_activitySearchable)
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
            replaceFragment(detailFragment, R.id.fragment_activitySearchable, true)
        }
    }

    private fun search(query: String) {
        launch {
            val searchFictionList = RoyalRoadApi.search(query)
            searchFictionList.forEach {
             logi("Searchable activity: ${it.name}")
            }
        }

//        GlobalScope.launch(Dispatchers.IO) {
//            searchFictionList.addAll(host.search(query))
//            withContext(Dispatchers.Default) {
//                fictionViewModel.fictionList.postValue(searchFictionList)
//            }
//        }
    }
}
