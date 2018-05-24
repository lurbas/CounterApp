package com.lucasurbas.counter.ui.explore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.lucasurbas.counter.R
import com.lucasurbas.counter.ui.explore.model.UiCounterItem
import com.lucasurbas.counter.ui.explore.model.UiListItem

class CountertemAdapterDelegate(
        val counterItemClickListener: (Int) -> Unit
) : AdapterDelegate<List<UiListItem>>() {

    public override fun isForViewType(items: List<UiListItem>, position: Int): Boolean {
        return items[position] is UiCounterItem
    }

    public override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return CounterItemViewHolder(parent)
    }

    override fun onBindViewHolder(items: List<UiListItem>,
                                  position: Int,
                                  holder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) {
        val counterItem = items[position] as UiCounterItem
        (holder as CounterItemViewHolder).bindUiModel(counterItem, counterItemClickListener)
    }

    internal class CounterItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_item_counter, parent, false)) {

        @BindView(R.id.value_view) @JvmField
        var valueView: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindUiModel(uiCounterItem: UiCounterItem, clickListener: (Int) -> Unit) {
            valueView?.text = uiCounterItem.stringValue

            itemView.setOnClickListener { clickListener(uiCounterItem.id) }
        }
    }
}
