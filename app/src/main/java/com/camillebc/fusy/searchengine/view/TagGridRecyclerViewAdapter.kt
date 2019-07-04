package com.camillebc.fusy.searchengine.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.camillebc.fusy.R
import com.camillebc.fusy.bookshelf.view.FictionGridFragment
import com.camillebc.fusy.core.model.Fiction
import com.camillebc.fusy.core.model.Tag
import kotlinx.android.synthetic.main.tag_detail.view.*

/**
 * [RecyclerView.Adapter] that can display a [Fiction] and makes a call to the
 * specified [FictionGridFragment.OnGridFragmentInteractionListener].
 */
class TagGridRecyclerViewAdapter(
    private var data: List<String>
) : RecyclerView.Adapter<TagGridRecyclerViewAdapter.ViewHolder>() {

    private val tagState: MutableMap<Int, Boolean> = mutableMapOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        with(holder) {
            fictionTag.text = item
            fictionTag.isChecked = tagState[adapterPosition] == false
        }
    }

    override fun getItemCount(): Int = data.size

    fun getCheckedTags(): List<String> =
        mutableListOf<String>().also { list ->
            tagState.forEach { if (it.value) list.add(data[it.key]) }
        }

    fun setData(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val fictionTag: CheckBox = view.checkBox_tagDetail


        override fun onClick(v: View?) {
            if (tagState[adapterPosition] == false) {
                fictionTag.isChecked = true
                tagState[adapterPosition] = true
            } else {
                fictionTag.isChecked = false
                tagState[adapterPosition] = false
            }
        }
    }
}
