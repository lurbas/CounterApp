package com.lucasurbas.counter.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.di.ActivityModule;
import com.lucasurbas.counter.app.di.helper.InjectHelper;
import com.lucasurbas.counter.ui.BaseActivity;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;
import com.lucasurbas.counter.ui.main.di.MainActivityModule;

public class MainActivity extends BaseActivity<MainActivityComponent> {

    @Inject
    MainNavigator navigator;

    @Override
    protected MainActivityComponent createComponent() {
        return InjectHelper.getAppComponent(this)
                .plus(new ActivityModule(this), new MainActivityModule());
    }

    @Override
    protected void onComponentCreated(@NonNull MainActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            navigator.navigateToExploreScreen();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navigator.navigateUp();
        return true;
    }
}
