package com.lucasurbas.counter.app.di

import android.content.Context
import com.lucasurbas.counter.app.di.rx.RxModule
import com.lucasurbas.counter.app.di.vm.ViewModelModule
import com.lucasurbas.counter.data.repository.di.RepositoryModule
import com.lucasurbas.counter.service.di.RunningCounterServiceComponent
import com.lucasurbas.counter.service.di.RunningCounterServiceModule
import com.lucasurbas.counter.ui.main.di.MainActivityComponent
import com.lucasurbas.counter.ui.main.di.MainActivityModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class, RxModule::class, ViewModelModule::class, RepositoryModule::class))
@Singleton
interface AppComponent {

    fun returnContext(): Context

    fun plus(activityModule: ActivityModule, mainActivityModule: MainActivityModule): MainActivityComponent

    operator fun plus(runningCounterServiceModule: RunningCounterServiceModule): RunningCounterServiceComponent

}
