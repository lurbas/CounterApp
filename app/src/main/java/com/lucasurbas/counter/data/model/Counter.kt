package com.lucasurbas.counter.data.model

data class Counter(
        val id: Int,
        val value: Long,
        val isRunning: Boolean = false
) {
    fun isIdEqual(id: Int): Boolean {
        return this.id == id
    }
}
