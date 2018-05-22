package com.lucasurbas.counter.rx;

import io.reactivex.Scheduler;

public interface InteractorSchedulers {

    Scheduler getBackgroundScheduler();

    Scheduler getMainThreadScheduler();
}
