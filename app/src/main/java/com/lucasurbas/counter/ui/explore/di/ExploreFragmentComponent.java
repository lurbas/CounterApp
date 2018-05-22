package com.lucasurbas.counter.ui.explore.di;

import dagger.Subcomponent;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.scope.FragmentScope;
import com.lucasurbas.counter.ui.explore.ExploreFragment;

@Subcomponent(modules = {
        FragmentModule.class, ExploreFragmentModule.class
})
@FragmentScope
public interface ExploreFragmentComponent {

    void inject(ExploreFragment fragment);
}
