package com.lucasurbas.counter.ui.explore.model

data class UiHeaderItem(
        override val id: Int = ITEM_ID
) : UiListItem {

    companion object {
        private val ITEM_ID = 11223344
    }
}
