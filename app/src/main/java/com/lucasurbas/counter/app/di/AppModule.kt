package com.lucasurbas.counter.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(context: Context) {

    private val appContext: Context

    init {
        appContext = context.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideApplicationContext(): Context {
        return appContext
    }
}
