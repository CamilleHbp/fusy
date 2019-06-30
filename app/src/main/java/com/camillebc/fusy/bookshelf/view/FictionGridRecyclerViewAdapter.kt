package com.camillebc.fusy.bookshelf.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.camillebc.fusy.R
import com.camillebc.fusy.core.model.Fiction
import kotlinx.android.synthetic.main.fragment_fiction_grid_detail.view.*

/**
 * [RecyclerView.Adapter] that can display a [Fiction] and makes a call to the
 * specified [FictionGridFragment.OnGridFragmentInteractionListener].
 */
class FictionGridRecyclerViewAdapter(
    private var data: List<Fiction>,
    private val listener: FictionGridFragment.OnGridFragmentInteractionListener?,
    private val glide: RequestManager
) : RecyclerView.Adapter<FictionGridRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Fiction
            // Notify the active callbacks interface (the activity, if the fragment is attached to one)
            // that an item has been selected.
            listener?.onGridFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fiction_grid_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        with(holder) {
            title.text = item.name
            unread.text = (item.chaptersTotal - item.lastChapterRead).toString()
            glide.load(item.imageUrl).into(image)
        }

        // Set the click listener with the item as view's tag
        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<Fiction>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.imageView_fragmentFictionGrid
        val title: TextView = view.textView_fragmentFictionGrid_title
        val unread: TextView = view.textView_fragmentFictionGrid_unread
    }
}
