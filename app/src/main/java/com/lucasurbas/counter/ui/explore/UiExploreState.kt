package com.lucasurbas.counter.ui.explore

import com.lucasurbas.counter.ui.explore.model.UiCounterItem
import com.lucasurbas.counter.ui.explore.model.UiHeaderItem
import com.lucasurbas.counter.ui.explore.model.UiListItem

data class UiExploreState(
        val itemList: List<UiListItem> = ArrayList(),
        val error: Throwable? = null
) {
    interface Part {

        fun computeNewState(previousState: UiExploreState): UiExploreState

        class Error(private val error: Throwable) : UiExploreState.Part {

            override fun computeNewState(previousState: UiExploreState): UiExploreState {
                return previousState.copy(error = error)
            }
        }

        class CounterList(private val uiCounterItemList: List<UiCounterItem>) : UiExploreState.Part {

            override fun computeNewState(previousState: UiExploreState): UiExploreState {
                return previousState.copy(error = null, itemList = calculateListData(uiCounterItemList))
            }

            private fun calculateListData(uiCounterItemList: List<UiCounterItem>): List<UiListItem> {
                val data = ArrayList<UiListItem>()
                data.add(UiHeaderItem())
                data.addAll(uiCounterItemList)
                return data
            }
        }
    }
}
