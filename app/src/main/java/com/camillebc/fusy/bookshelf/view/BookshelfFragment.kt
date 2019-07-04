package com.camillebc.fusy.bookshelf.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.camillebc.fusy.R
import com.camillebc.fusy.core.model.Fiction
import com.camillebc.fusy.core.model.FictionViewModel
import com.camillebc.fusy.utilities.logi
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.camillebc.utilities.RecyclerViewEmptySupport

/**
 * A [Fragment] subclass that manages a child [FictionGridFragment].
 */
class BookshelfFragment :
    Fragment(),
    CoroutineScope by CoroutineScope(Dispatchers.IO),
    FictionGridFragment.OnGridFragmentInteractionListener {
    private var columnCount = 3
    private var category: String? = null
    private var fetchFictionsJob: Job? = null
    private lateinit var fictionViewModel: FictionViewModel
    private lateinit var fictionGridView: RecyclerViewEmptySupport
    private lateinit var tabLayout: TabLayout


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fictionViewModel = ViewModelProviders.of(this.activity!!).get(FictionViewModel::class.java)
        setTabLayout()
        setGridView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        logi("OnCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookshelf, container, false)
    }

    override fun onResume() {
        super.onResume()
        logi("OnResume")
        fetchFictionsJob?.cancel()
        fetchFictionsJob = launch {
            fictionViewModel.setBookshelfList(category)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)

        // Get the SearchView and set the searchable configuration
        activity?.run {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
        }
    }


    private fun setTabLayout() {
        tabLayout = tabLayout_fragmentBookshelf
        with(tabLayout) {
            launch {
                fictionViewModel.getBookshelfCategories()?.forEach {
                    this@with.newTab().text = it
                }
            }
            addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.text.let {
                        when (it) {
                            tabItem_fragmentBookshelf_default.text -> category = null
                            else -> category = it?.toString()
                        }
                    }
                    fetchFictionsJob = launch {
                        fictionViewModel.setBookshelfList(category)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun setGridView() {
        fictionGridView = recyclerView_fragmentBookshelf
        val requestOptions = RequestOptions().apply {
            placeholder(R.drawable.fiction_placeholder_royalroad)
            error(R.drawable.fiction_placeholder_royalroad)
        }
        with(fictionGridView) {
            layoutManager = when {
                columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
            }
            val bookshelfAdapter = FictionGridRecyclerViewAdapter(
                fictionViewModel.fictionBookshelfList.value!!,
                this@BookshelfFragment,
                Glide.with(this).setDefaultRequestOptions(requestOptions)
            )
            val bookshelfObserver = Observer<MutableList<Fiction>> {
                bookshelfAdapter.setData(it)
            }
            adapter = bookshelfAdapter
            setEmptyView(textView_fragmentBookshelf_empty)
            fictionViewModel.fictionBookshelfList.observe(viewLifecycleOwner, bookshelfObserver)
        }
    }

    override fun onGridFragmentInteraction(item: Fiction?) {
        item?.let {
            fictionViewModel.fictionDetail.postValue(it)
            findNavController().navigate(R.id.action_bookshelfFragment_to_fictionDetailFragment)
        }
    }

}
