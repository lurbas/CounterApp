package com.lucasurbas.counter.ui.explore.di

import com.lucasurbas.counter.app.di.FragmentModule
import com.lucasurbas.counter.app.di.scope.FragmentScope
import com.lucasurbas.counter.ui.explore.ExploreFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class, ExploreFragmentModule::class))
@FragmentScope
interface ExploreFragmentComponent {

    fun inject(fragment: ExploreFragment)
}
