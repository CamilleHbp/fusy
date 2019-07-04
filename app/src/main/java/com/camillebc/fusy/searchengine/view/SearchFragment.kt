package com.camillebc.fusy.searchengine.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.camillebc.fusy.R
import com.camillebc.fusy.core.model.FictionViewModel
import com.camillebc.fusy.utilities.notifyObserver
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.camillebc.utilities.RecyclerViewEmptySupport

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchFragment.OnSearchFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class SearchFragment : Fragment(), CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private var columnCount = 4
    private var listenerSearch: OnSearchFragmentInteractionListener? = null
    private val tagList = MutableLiveData<List<String>>(listOf())
    private lateinit var tagGridView: RecyclerViewEmptySupport
    private lateinit var fictionViewModel: FictionViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fictionViewModel = ViewModelProviders.of(this.activity!!).get(FictionViewModel::class.java)
        updateTags()
        setTagGrid()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchFragmentInteractionListener) {
            listenerSearch = context
        } else {
            throw RuntimeException("$context must implement OnSearchFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerSearch = null
    }

    override fun onResume() {
        super.onResume()
        updateTags()
    }

    private fun setTagGrid() {
        tagGridView = recyclerView_fragmentSearch
        with(tagGridView) {
            layoutManager = when {
                columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
            }
            val tagGridAdapter = TagGridRecyclerViewAdapter(
                tagList.value!!
            )
            val tagObserver = Observer<List<String>> {
                tagGridAdapter.setData(it)
            }
            adapter = tagGridAdapter
            setEmptyView(textView_fragmentBookshelf_empty)
            tagList.observe(viewLifecycleOwner, tagObserver)
        }
    }

    private fun updateTags() {
        with(tagList) {
            launch {
                postValue(fictionViewModel.getTags())
                notifyObserver()
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnSearchFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onSearchFragmentInteraction(uri: Uri)
    }

}
