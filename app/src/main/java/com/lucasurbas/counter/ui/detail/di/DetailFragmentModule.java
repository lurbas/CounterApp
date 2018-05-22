package com.lucasurbas.counter.ui.detail.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import com.lucasurbas.counter.app.di.vm.ViewModelFactory;
import com.lucasurbas.counter.app.di.vm.ViewModelKey;
import com.lucasurbas.counter.ui.detail.DetailPresenter;
import com.lucasurbas.counter.ui.detail.usecase.GetPostInteractor;
import com.lucasurbas.counter.ui.detail.usecase.MarkPostAsReadInteractor;

@Module
public class DetailFragmentModule {

    @Provides
    DetailPresenter provideDetailPresenter(Fragment fragment, ViewModelFactory viewModelFactory) {
        return ViewModelProviders.of(fragment, viewModelFactory).get(DetailPresenter.class);
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailPresenter.class)
    ViewModel provideViewModel(GetPostInteractor getPostInteractor,
            MarkPostAsReadInteractor markPostAsReadInteractor) {
        return new DetailPresenter(getPostInteractor, markPostAsReadInteractor);
    }
}
