package com.lucasurbas.counter.ui.explore.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment

import com.lucasurbas.counter.app.di.vm.ViewModelFactory
import com.lucasurbas.counter.app.di.vm.ViewModelKey
import com.lucasurbas.counter.ui.explore.ExplorePresenter
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper
import com.lucasurbas.counter.ui.explore.usecase.GetCountersUpdatesInteractor

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ExploreFragmentModule {

    @Provides
    internal fun provideExplorePresenter(fragment: Fragment, viewModelFactory: ViewModelFactory): ExplorePresenter {
        return ViewModelProviders.of(fragment, viewModelFactory).get(ExplorePresenter::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(ExplorePresenter::class)
    internal fun provideViewModel(getCountersUpdatesInteractor: GetCountersUpdatesInteractor,
                                  uiMapper: UiCounterItemMapper): ViewModel {
        return ExplorePresenter(getCountersUpdatesInteractor, uiMapper)
    }
}
