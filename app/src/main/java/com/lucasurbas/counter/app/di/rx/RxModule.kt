package com.lucasurbas.counter.app.di.rx

import dagger.Binds
import dagger.Module

@Module
abstract class RxModule {

    @Binds
    internal abstract fun bindInteractorSchedulers(schedulers: AndroidInteractorSchedulers): InteractorSchedulers
}
