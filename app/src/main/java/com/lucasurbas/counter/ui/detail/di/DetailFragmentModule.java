package com.lucasurbas.counter.ui.detail.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import com.lucasurbas.counter.app.di.vm.ViewModelFactory;
import com.lucasurbas.counter.app.di.vm.ViewModelKey;
import com.lucasurbas.counter.ui.detail.DetailPresenter;
import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper;
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class DetailFragmentModule {

    @Provides
    DetailPresenter provideDetailPresenter(Fragment fragment, ViewModelFactory viewModelFactory) {
        return ViewModelProviders.of(fragment, viewModelFactory).get(DetailPresenter.class);
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailPresenter.class)
    ViewModel provideViewModel(GetCounterUpdateInteractor getCounterUpdateInteractor,
                               UiCounterDetailMapper uiMapper) {
        return new DetailPresenter(getCounterUpdateInteractor, uiMapper);
    }
}
