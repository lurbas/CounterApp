package com.lucasurbas.counter.app.di.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.lucasurbas.counter.app.di.ActivityComponent;
import com.lucasurbas.counter.app.di.AppComponent;

public class InjectHelper {

    private InjectHelper() {
    }

    /**
     * Returns component provided by given Activity.
     *
     * @param componentType Class of component
     * @param o             Object providing the component must be implementing {@code
     *                      HasComponent}
     * @param <C>           Type of component
     * @return Component instance
     */
    @SuppressWarnings("unchecked")
    public static <C> C getComponent(@NonNull final Class<C> componentType,
            @NonNull final Object o) {
        return componentType.cast(((HasComponent<C>) o).getComponent());
    }

    @NonNull
    public static ActivityComponent getActivityComponent(@NonNull final Activity activity) {
        return getComponent(ActivityComponent.class, activity);
    }

    @NonNull
    public static AppComponent getAppComponent(@NonNull final Context context) {
        return getComponent(AppComponent.class, context.getApplicationContext());
    }
}
