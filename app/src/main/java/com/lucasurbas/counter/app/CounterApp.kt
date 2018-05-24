package com.lucasurbas.counter.app

import android.app.Application

import com.lucasurbas.counter.app.di.AppComponent
import com.lucasurbas.counter.app.di.AppModule
import com.lucasurbas.counter.app.di.DaggerAppComponent
import com.lucasurbas.counter.app.di.helper.HasComponent

class CounterApp : Application(), HasComponent<AppComponent> {

    override var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        component = createComponent()
    }

    private fun createComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

}
