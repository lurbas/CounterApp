package com.lucasurbas.counter.ui.main.di

import android.support.v4.app.FragmentActivity
import com.lucasurbas.counter.app.di.scope.ActivityScope
import com.lucasurbas.counter.ui.main.MainNavigator
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    @ActivityScope
    internal fun provideMainNavigator(activity: FragmentActivity): MainNavigator {
        return MainNavigator(activity)
    }
}
