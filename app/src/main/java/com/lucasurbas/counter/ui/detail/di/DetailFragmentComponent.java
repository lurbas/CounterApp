package com.lucasurbas.counter.ui.detail.di;

import dagger.Subcomponent;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.scope.FragmentScope;
import com.lucasurbas.counter.ui.detail.DetailFragment;

@Subcomponent(modules = {
        FragmentModule.class, DetailFragmentModule.class
})
@FragmentScope
public interface DetailFragmentComponent {

    void inject(DetailFragment fragment);
}
