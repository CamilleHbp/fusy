package com.camillebc.fusy

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FictionListFragment
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.addFragment
import com.camillebc.fusy.utilities.notifyObserver
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

    override fun onListFragmentInteraction(item: Fiction?) {
        if (item != null) {
            Log.i(TAG, item.name)
            Toast.makeText(this, "Selected: ${item.description}", Toast.LENGTH_SHORT).show()
            GlobalScope.launch(Dispatchers.IO) {
                val fiction = host.getFiction(item.hostId)
//                val tags = host.getFictionTags(item.hostId)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchableActivity, "Fiction updated: ${item.name}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                Toast.makeText(this, "SEARCHING...", Toast.LENGTH_SHORT).show()
                search(query)
            }
        }

        fictionViewModel = ViewModelProviders.of(this).get(FictionViewModel::class.java)
        val favoriteFragment = FictionListFragment()
        addFragment(favoriteFragment, R.id.search_layout)
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
