package com.lucasurbas.counter.ui.main.di

import dagger.Module
import dagger.Provides
import com.lucasurbas.counter.app.di.scope.ActivityScope
import com.lucasurbas.counter.ui.BaseActivity
import com.lucasurbas.counter.ui.main.MainNavigator

@Module
class MainActivityModule {

    @Provides
    @ActivityScope
    internal fun provideMainNavigator(baseActivity: BaseActivity<*>): MainNavigator {
        return MainNavigator(baseActivity)
    }
}
