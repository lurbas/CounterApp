package com.lucasurbas.counter.service

import android.os.CountDownTimer
import java.util.*

class AndroidTimers : Timers {

    companion object {

        private val INTERVAL: Long = 51
    }

    private val timerMap: MutableMap<Int, CountDownTimer>

    init {
        this.timerMap = HashMap()
    }

    override fun startNew(timerId: Int, millisInFuture: Long, listener: Timers.TimerListener) {
        val timer = object : CountDownTimer(millisInFuture, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                listener.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                listener.onFinish()
            }
        }
        timerMap[timerId] = timer
        timer.start()
    }

    override fun remove(timerId: Int) {
        val timer = timerMap[timerId]
        timer?.cancel()
        timerMap.remove(timerId)
    }

    override fun hasTimer(timerId: Int): Boolean {
        return timerMap.containsKey(timerId)
    }
}
