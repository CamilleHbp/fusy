package com.camillebc.fusy.fragments


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.camillebc.fusy.R
import com.camillebc.fusy.data.Fiction
import com.camillebc.fusy.fragments.FictionListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_fiction.view.*

/**
 * [RecyclerView.Adapter] that can display a [Fiction] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class FictionListRecyclerViewAdapter(
    private var data: List<Fiction>,
    private val mListener: OnListFragmentInteractionListener?,
    private val glide: RequestManager
) : androidx.recyclerview.widget.RecyclerView.Adapter<FictionListRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Fiction
            // Notify the active callbacks interface (the activity, if the fragment is attached to one)
            // that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FictionListRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fiction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.name
        glide.load(item.imageUrl).into(holder.image)

        // Set the click listener with the item as view's tag
        with(holder.view) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<Fiction>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val image: ImageView = view.fictionCard_image
        val title: TextView = view.fictionCard_title
    }
}
