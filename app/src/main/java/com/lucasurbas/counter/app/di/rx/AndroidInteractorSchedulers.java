package com.lucasurbas.counter.app.di.rx;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AndroidInteractorSchedulers implements InteractorSchedulers {

    @Inject
    public AndroidInteractorSchedulers() {
    }

    @Override
    public Scheduler getBackgroundScheduler() {
        return Schedulers.io();
    }

    @Override
    public Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
