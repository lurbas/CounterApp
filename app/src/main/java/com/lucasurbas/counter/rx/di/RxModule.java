package com.lucasurbas.counter.rx.di;

import dagger.Binds;
import dagger.Module;
import com.lucasurbas.counter.rx.AndroidInteractorSchedulers;
import com.lucasurbas.counter.rx.InteractorSchedulers;

@Module
public abstract class RxModule {

    @Binds
    abstract InteractorSchedulers bindInteractorSchedulers(AndroidInteractorSchedulers schedulers);
}
