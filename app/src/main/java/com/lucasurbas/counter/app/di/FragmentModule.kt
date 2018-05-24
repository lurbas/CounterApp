package com.lucasurbas.counter.app.di

import android.support.v4.app.Fragment
import com.lucasurbas.counter.app.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @FragmentScope
    internal fun provideFragment(): Fragment {
        return fragment
    }
}
