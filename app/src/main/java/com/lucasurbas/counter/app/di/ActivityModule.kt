package com.lucasurbas.counter.app.di

import android.support.v4.app.FragmentActivity

import com.lucasurbas.counter.app.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: FragmentActivity) {

    @Provides
    @ActivityScope
    internal fun provideActivity(): FragmentActivity {
        return activity
    }
}
