package com.lucasurbas.counter.app.di;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import com.lucasurbas.counter.app.di.scope.FragmentScope;

@Module
public class FragmentModule {

    private final Fragment fragment;

    public FragmentModule(@NonNull final Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    Fragment provideFragment() {
        return fragment;
    }
}
