package com.lucasurbas.counter.ui.explore.model

data class UiCounterItem(
        override val id: Int,
        val stringValue: String,
        val isRunning: Boolean,
        val color: String
) : UiListItem
