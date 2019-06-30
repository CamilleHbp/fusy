package com.camillebc.fusy.searchengine

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.R
import com.camillebc.fusy.bookshelf.view.FictionDetailFragment
import com.camillebc.fusy.core.APP_TAG
import com.camillebc.fusy.core.di.Injector
import com.camillebc.fusy.core.model.Fiction
import com.camillebc.fusy.core.model.FictionViewModel
import com.camillebc.fusy.searchengine.view.FictionListFragment
import com.camillebc.fusy.utilities.logi
import com.camillebc.fusy.utilities.notifyObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import me.camillebc.fictionproviderapi.ApiProvider
import me.camillebc.fictionproviderapi.FictionMetadata
import me.camillebc.fictionproviderapi.FictionProvider
import me.camillebc.utilities.extensions.addFragment
import me.camillebc.utilities.extensions.replaceFragment

private const val TAG = APP_TAG + "SearchableActivity"

class SearchableActivity :
    AppCompatActivity(),
    FictionListFragment.OnListFragmentInteractionListener,
    FictionDetailFragment.OnDetailFragmentInteractionListener,
    CoroutineScope by CoroutineScope(Dispatchers.IO) {

    private lateinit var fictionViewModel: FictionViewModel
    private var searchJob: Job? = null

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
        addFragment(R.id.fragment_activitySearchable, favoriteFragment)
    }

    override fun onStop() {
        searchJob?.cancel()
        super.onStop()
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

    override fun onAdd(item: Fiction) {
        launch { fictionViewModel.addFictionToRepository(item) }
        Toast.makeText(this, "${item.name} added to the library.", Toast.LENGTH_SHORT).show()
    }

    override fun onRead(item: Fiction) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListFragmentInteraction(item: FictionMetadata?) {
        if (item != null) {
            fictionViewModel.fictionDetail.value = Fiction(item)
            replaceFragment(R.id.fragment_activitySearchable, FictionDetailFragment(), true)
        }
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    private fun search(query: String) {
        searchJob = launch {
            val api = ApiProvider.getApi(FictionProvider.ROYALROAD)
            val searchFictionList = api.search(query)
            searchFictionList.consumeEach { fiction ->
                logi("Searchable activity: ${fiction.name}")
                withContext(Dispatchers.Default) {
                    fictionViewModel.fictionSearchList.value?.add(fiction)
                    fictionViewModel.fictionSearchList.notifyObserver()
                }
            }
        }
    }
}
