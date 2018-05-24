package com.lucasurbas.counter.ui.detail.model

data class UiCounterDetail(
        val id: Int,
        val valueInMillis: Long,
        val isRunning: Boolean,
        val color: String
)
