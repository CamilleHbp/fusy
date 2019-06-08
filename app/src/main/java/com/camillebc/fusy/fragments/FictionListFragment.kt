package com.camillebc.fusy.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.camillebc.fusy.R
import com.camillebc.fusy.model.Fiction
import com.camillebc.fusy.model.FictionViewModel
import com.camillebc.fusy.utilities.APP_TAG
import com.camillebc.fusy.utilities.RecyclerViewEmptySupport
import kotlinx.android.synthetic.main.fragment_fiction_list.*

private const val TAG = APP_TAG + "FictionListFragment"

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FictionListFragment.OnListFragmentInteractionListener] interface.
 */
class FictionListFragment : androidx.fragment.app.Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var fictionListView: RecyclerViewEmptySupport
    private lateinit var fictionModel: FictionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    /**
     * [onActivityCreated] is called AFTER [onCreateView] and AFTER the parent activity's [onCreate]
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val requestOptions = RequestOptions().apply{
            placeholder(R.drawable.fiction_placeholder_royalroad)
            error(R.drawable.fiction_placeholder_royalroad)
        }
        fictionModel = ViewModelProviders.of(this.activity!!).get(FictionViewModel::class.java)
        fictionListView = recyclerView_fictionList
        with(fictionListView) {
            layoutManager = when {
                columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
            }
            val favoritesAdapter = FictionListRecyclerViewAdapter(
                fictionModel.fictionList.value!!,
                listener,
                Glide.with(this).setDefaultRequestOptions(requestOptions)
            )
            val favoritesObserver = Observer<MutableList<Fiction>> {
                favoritesAdapter.setData(it)
            }
            adapter = favoritesAdapter
            setEmptyView(textView_searchableEmpty)
            fictionModel.fictionList.observe(this@FictionListFragment, favoritesObserver)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set the adapter
        return inflater.inflate(R.layout.fragment_fiction_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Fiction?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FictionListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
