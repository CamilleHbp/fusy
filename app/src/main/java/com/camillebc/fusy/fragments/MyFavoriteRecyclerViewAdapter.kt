package com.camillebc.fusy.fragments

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.camillebc.fusy.R
import com.camillebc.fusy.data.FictionData


import com.camillebc.fusy.fragments.FavoriteFragment.OnListFragmentInteractionListener
import com.camillebc.fusy.utilities.BackgroundThread
import com.camillebc.fusy.utilities.ImageLoader

import kotlinx.android.synthetic.main.fragment_favorite.view.*

/**
 * [RecyclerView.Adapter] that can display a [FictionData] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyFavoriteRecyclerViewAdapter(
    private var data: List<FictionData>,
    private val mListener: OnListFragmentInteractionListener?,
    private val glide: RequestManager
) : RecyclerView.Adapter<MyFavoriteRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as FictionData
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoriteRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Alternate the row's colour
        if(position %2 == 1) holder.itemView.setBackgroundColor(Color.parseColor("#F4F4F4"))
        else holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))

        val item = data[position]
        holder.id.text = position.toString()
        holder.title.text = item.title
        glide
            .load(item.imageUrl)
            .into(holder.image)

        with(holder.view) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<FictionData>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.favorite_number
        val image: ImageView = view.favorite_image
        val title: TextView = view.favorite_title

        override fun toString(): String {
            return super.toString() + " '" + title.text + "'"
        }
    }
}
