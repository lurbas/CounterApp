package com.lucasurbas.counter.ui.detail.widget.di

import com.lucasurbas.counter.ui.detail.widget.CounterViewPresenter
import dagger.Module
import dagger.Provides

@Module
class CounterViewModule {

    @Provides
    internal fun provideDetailPresenter(): CounterViewPresenter {
        return CounterViewPresenter()
    }
}
