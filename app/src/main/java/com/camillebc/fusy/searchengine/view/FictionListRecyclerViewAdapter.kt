package com.camillebc.fusy.searchengine.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.camillebc.fusy.R
import com.camillebc.fusy.core.model.Fiction
import kotlinx.android.synthetic.main.fragment_fiction_list_detail.view.*
import me.camillebc.fictionproviderapi.FictionMetadata

/**
 * [RecyclerView.Adapter] that can display a [Fiction] and makes a call to the
 * specified [FictionListFragment.OnListFragmentInteractionListener].
 */
class FictionListRecyclerViewAdapter(
    private var data: List<FictionMetadata>,
    private val listener: FictionListFragment.OnListFragmentInteractionListener?,
    private val glide: RequestManager
) : RecyclerView.Adapter<FictionListRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as FictionMetadata
            // Notify the active callbacks interface (the activity, if the fragment is attached to one)
            // that an item has been selected.
            listener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fiction_list_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.name
        glide.load(item.imageUrl).into(holder.image)

        // Set the click listener with the item as view's tag
        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<FictionMetadata>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.imageView_fragmentFiction
        val title: TextView = view.textView_fragmentFiction_title
    }
}
