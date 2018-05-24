package com.lucasurbas.counter.app.di.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AndroidInteractorSchedulers @Inject constructor() : InteractorSchedulers {

    override val backgroundScheduler: Scheduler
        get() = Schedulers.io()

    override val mainThreadScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

    override val singleScheduler: Scheduler
        get() = Schedulers.single()
}
