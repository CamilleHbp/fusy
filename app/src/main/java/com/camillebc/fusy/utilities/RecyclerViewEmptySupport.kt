package com.camillebc.fusy.utilities

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TAG = APP_TAG + "RecyclerViewEmptySupport"

class RecyclerViewEmptySupport : RecyclerView {
    private var emptyView: View? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            checkIfEmpty()
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)

        adapter?.registerAdapterDataObserver(observer)
        super.setAdapter(adapter)
        checkIfEmpty()
    }

    override fun swapAdapter(adapter: RecyclerView.Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)

        adapter?.registerAdapterDataObserver(observer)
        super.swapAdapter(adapter, removeAndRecycleExistingViews)
        checkIfEmpty()
    }

    /**
     * Indicates the view to be shown when the adapter for this object is empty
     *
     * @param emptyView
     */
    fun setEmptyView(emptyView: View?) {
        if (this.emptyView != null) {
            this.emptyView!!.visibility = View.GONE
        }

        this.emptyView = emptyView
        checkIfEmpty()
    }

    /**
     * Check adapter item count and toggle visibility of empty view if the adapter is empty
     */
    private fun checkIfEmpty() {
        if (emptyView == null || adapter == null) {
            Log.i(TAG, "Empty view or adapter")
            return
        }

        if (adapter!!.itemCount > 0) {
            emptyView!!.visibility = View.GONE
        } else {
            emptyView!!.visibility = View.VISIBLE
        }
    }
}
