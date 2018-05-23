package com.lucasurbas.counter.app.di.rx;

import io.reactivex.Scheduler;

public interface InteractorSchedulers {

    Scheduler getBackgroundScheduler();

    Scheduler getMainThreadScheduler();

    Scheduler getSingleScheduler();
}
