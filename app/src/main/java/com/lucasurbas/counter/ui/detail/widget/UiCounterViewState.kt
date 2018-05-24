package com.lucasurbas.counter.ui.detail.widget

import java.util.concurrent.TimeUnit

data class UiCounterViewState(
        val value: Long? = null,
        val minuteLeft: Int = 0,
        val minuteRight: Int = 0,
        val secondLeft: Int = 0,
        val secondRight: Int = 0,
        val millis: Int = 0
) {
    interface Part {

        fun computeNewState(previousState: UiCounterViewState): UiCounterViewState

        class CounterValue(private val valueInMillis: Long) : UiCounterViewState.Part {

            override fun computeNewState(previousState: UiCounterViewState): UiCounterViewState {

                val minutes = valueInMillis / TimeUnit.MINUTES.toMillis(1)
                val minuteLeft = minutes / 10
                val minuteRight = minutes % 10
                val seconds = valueInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1)
                val secondLeft = seconds / 10
                val secondRight = seconds % 10
                val millis = valueInMillis % TimeUnit.SECONDS.toMillis(1) / 10

                return previousState.copy(
                        value = valueInMillis,
                        minuteLeft = minuteLeft.toInt(),
                        minuteRight = minuteRight.toInt(),
                        secondLeft = secondLeft.toInt(),
                        secondRight = secondRight.toInt(),
                        millis = millis.toInt()
                )
            }
        }
    }
}