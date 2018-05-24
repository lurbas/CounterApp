package com.lucasurbas.counter.ui.detail

import com.lucasurbas.counter.ui.detail.model.UiCounterDetail

data class UiDetailState(
        val counter: UiCounterDetail? = null,
        val error: Throwable? = null
) {
    interface Part {

        fun computeNewState(previousState: UiDetailState): UiDetailState

        class Error(private val error: Throwable) : UiDetailState.Part {

            override fun computeNewState(previousState: UiDetailState): UiDetailState {
                return previousState.copy(error = error)
            }
        }

        class CounterItem(private val counter: UiCounterDetail) : UiDetailState.Part {

            override fun computeNewState(previousState: UiDetailState): UiDetailState {
                return previousState.copy(counter = counter, error = null)
            }
        }

        class CounterUpdated : UiDetailState.Part {

            override fun computeNewState(previousState: UiDetailState): UiDetailState {
                return previousState
            }
        }
    }
}
