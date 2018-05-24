package com.lucasurbas.counter.ui.detail.widget.di

import com.lucasurbas.counter.app.di.scope.FragmentScope
import com.lucasurbas.counter.ui.detail.widget.CounterView
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(CounterViewModule::class))
@FragmentScope
interface CounterViewComponent {

    fun inject(view: CounterView)
}
