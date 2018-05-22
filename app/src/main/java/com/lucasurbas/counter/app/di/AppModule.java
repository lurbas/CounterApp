package com.lucasurbas.counter.app.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context appContext;

    public AppModule(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @Singleton
    @Provides
    Context getApplicationContext() {
        return appContext;
    }

}
