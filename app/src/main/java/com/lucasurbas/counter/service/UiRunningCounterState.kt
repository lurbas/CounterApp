package com.lucasurbas.counter.service

import com.lucasurbas.counter.ui.explore.model.UiCounterItem

data class UiRunningCounterState(
        val runningItemList: List<UiCounterItem>? = null,
        val error: Throwable? = null,
        val isStarted: Boolean = false
) {
    interface Part {

        fun computeNewState(previousState: UiRunningCounterState): UiRunningCounterState

        class Error(private val error: Throwable) : UiRunningCounterState.Part {

            override fun computeNewState(previousState: UiRunningCounterState): UiRunningCounterState {
                return previousState.copy(error = error)
            }
        }

        class RunningCounterList(private val uiCounterItemList: List<UiCounterItem>) : UiRunningCounterState.Part {

            override fun computeNewState(previousState: UiRunningCounterState): UiRunningCounterState {
                return previousState.copy(runningItemList = uiCounterItemList, error = null)
            }
        }

        class CounterUpdated : UiRunningCounterState.Part {

            override fun computeNewState(previousState: UiRunningCounterState): UiRunningCounterState {
                return previousState.copy(isStarted = true)
            }
        }
    }
}
