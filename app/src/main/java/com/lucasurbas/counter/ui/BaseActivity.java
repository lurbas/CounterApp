package com.lucasurbas.counter.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucasurbas.counter.app.di.helper.HasComponent;

public abstract class BaseActivity<C> extends AppCompatActivity implements HasComponent<C> {

    private C component;

    @NonNull
    @Override
    public C getComponent() {
        return component;
    }

    /**
     * Is called after the component has been created.
     * <p/>
     * Must be implemented to call the injection on the object, e.g. {@code
     * component.inject(this)}.
     * <p/>
     *
     * @param component Component which should be used to inject dependencies
     */
    protected void onComponentCreated(@NonNull final C component) {
    }

    /**
     * Creates component instance.
     */
    protected abstract C createComponent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        component = createComponent();
        if (component == null) {
            throw new NullPointerException("Component must not be null");
        }
        super.onCreate(savedInstanceState);
        onComponentCreated(component);
    }
}
