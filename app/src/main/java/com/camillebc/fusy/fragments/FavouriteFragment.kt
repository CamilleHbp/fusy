package com.camillebc.fusy.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.camillebc.fusy.R
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.data.RoyalroadViewModel

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FavouriteFragment.OnListFragmentInteractionListener] interface.
 */
class FavouriteFragment : androidx.fragment.app.Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_list, container, false)

        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                    else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                }
                val favoritesAdapter = MyFavoriteRecyclerViewAdapter(
//                    RoyalroadViewModel.favoriteList.value?: listOf<Fiction>(),
                    RoyalroadViewModel.favoriteList.value!!,
                    listener,
                    Glide.with(this)
                )
                val favoritesObserver = Observer<List<Fiction>> { favoritesAdapter.setData(it) }
                adapter = favoritesAdapter
                RoyalroadViewModel.favoriteList.observe(this@FavouriteFragment, favoritesObserver)
            }
        }
        return view
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
            FavouriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
