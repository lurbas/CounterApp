package com.lucasurbas.counter.app.di.vm

import android.arch.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
