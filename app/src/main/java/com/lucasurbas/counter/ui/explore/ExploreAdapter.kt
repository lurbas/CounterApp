package com.lucasurbas.counter.ui.explore


import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.lucasurbas.counter.ui.explore.model.UiListItem
import com.lucasurbas.counter.utils.ListItemDiffCallback
import java.util.*

class ExploreAdapter(
        counterItemClickListener: (Int, View) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TYPE_HEADER = 1
        private val TYPE_COUNTER = 2
    }

    private val delegatesManager: AdapterDelegatesManager<List<UiListItem>>
    private val itemList = ArrayList<UiListItem>()

    init {
        delegatesManager = AdapterDelegatesManager()
        delegatesManager
                .addDelegate(TYPE_HEADER, HeaderItemAdapterDelegate())
                .addDelegate(TYPE_COUNTER, CountertemAdapterDelegate(counterItemClickListener))
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(itemList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(itemList, position, holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].id.toLong()
    }

    fun getSpanSize(position: Int, spanCount: Int): Int {
        return when (getItemViewType(position)) {
            TYPE_HEADER -> spanCount
            else -> 1
        }
    }

    fun updateItemList(newItemList: List<UiListItem>) {
        val diffResult = DiffUtil.calculateDiff(ListItemDiffCallback(itemList, newItemList))
        itemList.clear()
        itemList.addAll(newItemList)
        diffResult.dispatchUpdatesTo(this)
    }
}
