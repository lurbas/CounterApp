package com.lucasurbas.counter.app.di.rx;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RxModule {

    @Binds
    abstract InteractorSchedulers bindInteractorSchedulers(AndroidInteractorSchedulers schedulers);
}
