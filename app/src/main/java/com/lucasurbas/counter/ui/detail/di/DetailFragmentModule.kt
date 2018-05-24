package com.lucasurbas.counter.ui.detail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment

import com.lucasurbas.counter.app.di.vm.ViewModelFactory
import com.lucasurbas.counter.app.di.vm.ViewModelKey
import com.lucasurbas.counter.service.usecase.GetCounterInteractor
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor
import com.lucasurbas.counter.ui.detail.DetailPresenter
import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class DetailFragmentModule {

    @Provides
    internal fun provideDetailPresenter(fragment: Fragment, viewModelFactory: ViewModelFactory): DetailPresenter {
        return ViewModelProviders.of(fragment, viewModelFactory).get(DetailPresenter::class.java)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailPresenter::class)
    internal fun provideViewModel(getCounterUpdateInteractor: GetCounterUpdateInteractor,
                                  getCounterInteractor: GetCounterInteractor,
                                  updateCounterInteractor: UpdateCounterInteractor,
                                  uiMapper: UiCounterDetailMapper): ViewModel {
        return DetailPresenter(getCounterUpdateInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiMapper)
    }
}
