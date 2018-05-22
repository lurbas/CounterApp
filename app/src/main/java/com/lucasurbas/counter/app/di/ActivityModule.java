package com.lucasurbas.counter.app.di;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import com.lucasurbas.counter.ui.BaseActivity;
import com.lucasurbas.counter.app.di.scope.ActivityScope;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(@NonNull final BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    BaseActivity provideActivity() {
        return activity;
    }
}
