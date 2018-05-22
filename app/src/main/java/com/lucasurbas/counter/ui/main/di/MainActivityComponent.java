package com.lucasurbas.counter.ui.main.di;

import dagger.Subcomponent;
import com.lucasurbas.counter.app.di.ActivityModule;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.scope.ActivityScope;
import com.lucasurbas.counter.ui.detail.di.DetailFragmentComponent;
import com.lucasurbas.counter.ui.detail.di.DetailFragmentModule;
import com.lucasurbas.counter.ui.explore.di.ExploreFragmentComponent;
import com.lucasurbas.counter.ui.explore.di.ExploreFragmentModule;
import com.lucasurbas.counter.ui.main.MainActivity;

@Subcomponent(modules = {
        ActivityModule.class, MainActivityModule.class
})
@ActivityScope
public interface MainActivityComponent {

    void inject(MainActivity activity);

    ExploreFragmentComponent plus(FragmentModule fragmentModule,
            ExploreFragmentModule exploreFragmentModule);

    DetailFragmentComponent plus(FragmentModule fragmentModule,
            DetailFragmentModule detailFragmentModule);
}
