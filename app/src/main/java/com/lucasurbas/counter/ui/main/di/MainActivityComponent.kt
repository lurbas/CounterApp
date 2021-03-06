package com.lucasurbas.counter.ui.main.di

import com.lucasurbas.counter.app.di.ActivityModule
import com.lucasurbas.counter.app.di.FragmentModule
import com.lucasurbas.counter.app.di.scope.ActivityScope
import com.lucasurbas.counter.ui.detail.di.DetailFragmentComponent
import com.lucasurbas.counter.ui.detail.di.DetailFragmentModule
import com.lucasurbas.counter.ui.detail.widget.di.CounterViewComponent
import com.lucasurbas.counter.ui.detail.widget.di.CounterViewModule
import com.lucasurbas.counter.ui.explore.di.ExploreFragmentComponent
import com.lucasurbas.counter.ui.explore.di.ExploreFragmentModule
import com.lucasurbas.counter.ui.main.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class, MainActivityModule::class))
@ActivityScope
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    fun plus(fragmentModule: FragmentModule,
             exploreFragmentModule: ExploreFragmentModule): ExploreFragmentComponent

    fun plus(fragmentModule: FragmentModule,
             detailFragmentModule: DetailFragmentModule): DetailFragmentComponent

    fun plus(counterViewModule: CounterViewModule): CounterViewComponent
}
