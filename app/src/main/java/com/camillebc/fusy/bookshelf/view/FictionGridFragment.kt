package com.camillebc.fusy.bookshelf.view

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
import com.camillebc.fusy.core.model.Fiction
import com.camillebc.fusy.core.model.FictionViewModel
import kotlinx.android.synthetic.main.fragment_fiction_grid.*
import me.camillebc.utilities.RecyclerViewEmptySupport

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FictionGridFragment.OnGridFragmentInteractionListener] interface.
 */
class FictionGridFragment : androidx.fragment.app.Fragment() {

    private var columnCount = 3
    private var listener: OnGridFragmentInteractionListener? = null
    private lateinit var fictionGridView: RecyclerViewEmptySupport
    private lateinit var fictionModel: FictionViewModel

    /**
     * [onActivityCreated] is called AFTER [onCreateView] and AFTER the parent activity's [onCreate]
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val requestOptions = RequestOptions().apply {
            placeholder(R.drawable.fiction_placeholder_royalroad)
            error(R.drawable.fiction_placeholder_royalroad)
        }
        fictionModel = ViewModelProviders.of(this.activity!!).get(FictionViewModel::class.java)
        fictionGridView = recyclerView_fragmentFictionGrid
        with(fictionGridView) {
            layoutManager = when {
                columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
            }
            val bookshelfAdapter = FictionGridRecyclerViewAdapter(
                fictionModel.fictionBookshelfList.value!!,
                listener,
                Glide.with(this).setDefaultRequestOptions(requestOptions)
            )
            val bookshelfObserver = Observer<MutableList<Fiction>> {
                bookshelfAdapter.setData(it)
            }
            adapter = bookshelfAdapter
            setEmptyView(progressBar_fragmentFictionGrid_empty)
            fictionModel.fictionBookshelfList.observe(viewLifecycleOwner, bookshelfObserver)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set the adapter
        return inflater.inflate(R.layout.fragment_fiction_grid, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            context is OnGridFragmentInteractionListener -> context
            parentFragment is OnGridFragmentInteractionListener -> parentFragment as OnGridFragmentInteractionListener
            else -> throw RuntimeException("$context must implement OnGridFragmentInteractionListener")
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
    interface OnGridFragmentInteractionListener {
        fun onGridFragmentInteraction(item: Fiction?)
    }
}
