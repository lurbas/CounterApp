package com.lucasurbas.counter.utils

import android.support.v7.util.DiffUtil

import com.lucasurbas.counter.ui.explore.model.UiListItem

class ListItemDiffCallback(private val current: List<UiListItem>, private val next: List<UiListItem>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return current.size
    }

    override fun getNewListSize(): Int {
        return next.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val currentItem = current[oldItemPosition]
        val nextItem = next[newItemPosition]
        return currentItem.id == nextItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val currentItem = current[oldItemPosition]
        val nextItem = next[newItemPosition]
        return currentItem == nextItem
    }
}
