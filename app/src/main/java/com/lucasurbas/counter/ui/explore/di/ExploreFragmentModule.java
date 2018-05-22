package com.lucasurbas.counter.ui.explore.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import com.lucasurbas.counter.app.di.vm.ViewModelFactory;
import com.lucasurbas.counter.app.di.vm.ViewModelKey;
import com.lucasurbas.counter.ui.explore.ExplorePresenter;
import com.lucasurbas.counter.ui.explore.usecase.GetPostsUpdatesInteractor;
import com.lucasurbas.counter.ui.explore.usecase.LoadPostsInteractor;
import com.lucasurbas.counter.ui.explore.mapper.UiPostMapper;

@Module
public class ExploreFragmentModule {

    @Provides
    ExplorePresenter provideExplorePresenter(Fragment fragment, ViewModelFactory viewModelFactory) {
        return ViewModelProviders.of(fragment, viewModelFactory).get(ExplorePresenter.class);
    }

    @Provides
    @IntoMap
    @ViewModelKey(ExplorePresenter.class)
    ViewModel provideViewModel(GetPostsUpdatesInteractor getPostsUpdatesInteractor,
            LoadPostsInteractor loadPostsInteractor, UiPostMapper uiPostMapper) {
        return new ExplorePresenter(getPostsUpdatesInteractor, loadPostsInteractor, uiPostMapper);
    }
}
