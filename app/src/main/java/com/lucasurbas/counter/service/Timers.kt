package com.lucasurbas.counter.service

interface Timers {

    interface TimerListener {

        fun onTick(millisUntilFinished: Long)

        fun onFinish()
    }

    fun startNew(timerId: Int, millisInFuture: Long, listener: TimerListener)

    fun remove(timerId: Int)

    fun hasTimer(timerId: Int): Boolean
}
