package com.lucasurbas.counter.ui.explore.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import com.lucasurbas.counter.app.di.vm.ViewModelFactory;
import com.lucasurbas.counter.app.di.vm.ViewModelKey;
import com.lucasurbas.counter.ui.explore.ExplorePresenter;
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper;
import com.lucasurbas.counter.ui.explore.usecase.GetCountersUpdatesInteractor;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ExploreFragmentModule {

    @Provides
    ExplorePresenter provideExplorePresenter(Fragment fragment, ViewModelFactory viewModelFactory) {
        return ViewModelProviders.of(fragment, viewModelFactory).get(ExplorePresenter.class);
    }

    @Provides
    @IntoMap
    @ViewModelKey(ExplorePresenter.class)
    ViewModel provideViewModel(GetCountersUpdatesInteractor getCountersUpdatesInteractor,
                               UiCounterItemMapper uiMapper) {
        return new ExplorePresenter(getCountersUpdatesInteractor, uiMapper);
    }
}
