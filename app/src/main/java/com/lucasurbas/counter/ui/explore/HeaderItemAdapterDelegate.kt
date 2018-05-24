package com.lucasurbas.counter.ui.explore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.lucasurbas.counter.R
import com.lucasurbas.counter.ui.explore.model.UiHeaderItem
import com.lucasurbas.counter.ui.explore.model.UiListItem

class HeaderItemAdapterDelegate : AdapterDelegate<List<UiListItem>>() {

    public override fun isForViewType(items: List<UiListItem>, position: Int): Boolean {
        return items[position] is UiHeaderItem
    }

    public override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return HeaderItemViewHolder(parent)
    }

    override fun onBindViewHolder(items: List<UiListItem>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
    }

    internal class HeaderItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_item_header, parent, false)) {
    }
}
