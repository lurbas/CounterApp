package com.lucasurbas.counter.ui.detail.widget

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel
import java.util.concurrent.TimeUnit

@PaperParcel
data class UiCounterViewState(
        val value: Long? = null,
        val minuteLeft: Int = 0,
        val minuteLeftAnimate: Boolean = false,
        val minuteRight: Int = 0,
        val minuteRightAnimate: Boolean = false,
        val secondLeft: Int = 0,
        val secondLeftAnimate: Boolean = false,
        val secondRight: Int = 0,
        val secondRightAnimate: Boolean = false,
        val millis: Int = 0
) : Parcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelUiCounterViewState.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelUiCounterViewState.writeToParcel(this, dest, flags)
    }

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

                val minuteLeftAnimate = previousState.minuteLeft != minuteLeft.toInt()
                val minuteRightAnimate = previousState.minuteRight != minuteRight.toInt()
                val secondLeftAnimate = previousState.secondLeft != secondLeft.toInt()
                val secondRightAnimate = previousState.secondRight != secondRight.toInt()

                return previousState.copy(
                        value = valueInMillis,
                        minuteLeft = minuteLeft.toInt(),
                        minuteLeftAnimate = minuteLeftAnimate,
                        minuteRight = minuteRight.toInt(),
                        minuteRightAnimate = minuteRightAnimate,
                        secondLeft = secondLeft.toInt(),
                        secondLeftAnimate = secondLeftAnimate,
                        secondRight = secondRight.toInt(),
                        secondRightAnimate = secondRightAnimate,
                        millis = millis.toInt()
                )
            }
        }
    }
}