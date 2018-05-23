package com.lucasurbas.counter.service;

import android.os.CountDownTimer;

import java.util.HashMap;
import java.util.Map;

public class AndroidTimers implements Timers {

    private static final long INTERVAL = 51;

    private Map<Integer, CountDownTimer> timerMap;

    public AndroidTimers() {
        this.timerMap = new HashMap<>();
    }

    @Override
    public void startNew(int timerId, long millisInFuture, TimerListener listener) {
        CountDownTimer timer = new CountDownTimer(millisInFuture, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                listener.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                listener.onFinish();
            }
        };
        timerMap.put(timerId, timer);
        timer.start();
    }

    @Override
    public void remove(int timerId) {
        CountDownTimer timer = timerMap.get(timerId);
        timer.cancel();
        timerMap.remove(timerId);
    }

    @Override
    public boolean hasTimer(int timerId) {
        return timerMap.containsKey(timerId);
    }
}
