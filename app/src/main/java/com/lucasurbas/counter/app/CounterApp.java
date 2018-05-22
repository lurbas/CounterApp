package com.lucasurbas.counter.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.lucasurbas.counter.app.di.AppComponent;
import com.lucasurbas.counter.app.di.AppModule;
import com.lucasurbas.counter.app.di.DaggerAppComponent;
import com.lucasurbas.counter.app.di.helper.HasComponent;

public class CounterApp extends Application implements HasComponent<AppComponent> {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();
    }

    private AppComponent createComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @NonNull
    @Override
    public AppComponent getComponent() {
        return component;
    }

}
