package com.lucasurbas.counter.ui.detail.di

import com.lucasurbas.counter.app.di.FragmentModule
import com.lucasurbas.counter.app.di.scope.FragmentScope
import com.lucasurbas.counter.ui.detail.DetailFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FragmentModule::class, DetailFragmentModule::class))
@FragmentScope
interface DetailFragmentComponent {

    fun inject(fragment: DetailFragment)
}
