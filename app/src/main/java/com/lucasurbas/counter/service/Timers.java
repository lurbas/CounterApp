package com.lucasurbas.counter.service;

public interface Timers {

    interface TimerListener {

        void onTick(long millisUntilFinished);

        void onFinish();
    }

    void startNew(int timerId, long millisInFuture, TimerListener listener);

    void remove(int timerId);

    boolean hasTimer(int timerId);
}
