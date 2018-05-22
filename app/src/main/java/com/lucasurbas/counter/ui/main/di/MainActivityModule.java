package com.lucasurbas.counter.ui.main.di;

import dagger.Module;
import dagger.Provides;
import com.lucasurbas.counter.app.di.scope.ActivityScope;
import com.lucasurbas.counter.ui.BaseActivity;
import com.lucasurbas.counter.ui.main.MainNavigator;

@Module
public class MainActivityModule {

    @Provides
    @ActivityScope
    MainNavigator provideMainNavigator(BaseActivity baseActivity) {
        return new MainNavigator(baseActivity);
    }
}
