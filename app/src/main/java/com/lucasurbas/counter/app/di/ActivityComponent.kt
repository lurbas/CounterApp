package com.lucasurbas.counter.app.di

import com.lucasurbas.counter.app.di.scope.ActivityScope
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ActivityModule::class))
@ActivityScope
interface ActivityComponent
