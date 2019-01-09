package com.camillebc.fusy

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.FictionRepository
import com.camillebc.fusy.data.FictionViewModel
import com.camillebc.fusy.di.Injector
import com.camillebc.fusy.fragments.FictionListFragment
import com.camillebc.fusy.interfaces.FictionHostInterface
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.addFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = APP_TAG + "SearchableActivity"

class SearchableActivity : AppCompatActivity(), FictionListFragment.OnListFragmentInteractionListener  {
    @Inject lateinit var repository: FictionRepository
    @Inject lateinit var host: FictionHostInterface
    private lateinit var fictionViewModel: FictionViewModel

    init {
        Injector.getFictionComponent().inject(this)
    }

    override fun onListFragmentInteraction(item: Fiction?) {
        if (item != null) {
            Log.i(TAG, item?.title)
            Toast.makeText(this, "Selected: ${item.url}", Toast.LENGTH_SHORT).show()
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
        GlobalScope.launch(Dispatchers.IO) {
            val results = host.search(query)

            withContext(Dispatchers.Default) {
                fictionViewModel.favoriteList.postValue(results)
            }
        }
    }
}
