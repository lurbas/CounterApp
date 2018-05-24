package com.lucasurbas.counter.ui.explore

import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.lucasurbas.counter.R
import com.lucasurbas.counter.ui.explore.model.UiCounterItem
import com.lucasurbas.counter.ui.explore.model.UiListItem

class CountertemAdapterDelegate(
        val counterItemClickListener: (Int, View) -> Unit
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
        @BindView(R.id.background_view) @JvmField
        var backgroundView: View? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindUiModel(uiCounterItem: UiCounterItem, clickListener: (Int, View) -> Unit) {
            valueView?.text = uiCounterItem.stringValue
            backgroundView?.setBackgroundColor(Color.parseColor(uiCounterItem.color))

            ViewCompat.setTransitionName(valueView, "backgroundImage${uiCounterItem.id}");

            itemView.setOnClickListener { clickListener(uiCounterItem.id, valueView as View) }
        }
    }
}
