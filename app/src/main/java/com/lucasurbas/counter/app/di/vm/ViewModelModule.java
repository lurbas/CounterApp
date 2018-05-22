package com.lucasurbas.counter.app.di.vm;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory viewModelFactory);
}
