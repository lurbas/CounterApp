package com.lucasurbas.counter.app.di.rx

import io.reactivex.Scheduler

interface InteractorSchedulers {

    val backgroundScheduler: Scheduler

    val mainThreadScheduler: Scheduler

    val singleScheduler: Scheduler
}
