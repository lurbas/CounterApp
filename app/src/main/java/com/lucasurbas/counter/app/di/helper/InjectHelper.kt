package com.lucasurbas.counter.app.di.helper

import android.app.Activity
import android.content.Context

import com.lucasurbas.counter.app.di.ActivityComponent
import com.lucasurbas.counter.app.di.AppComponent

object InjectHelper {

    /**
     * Returns component provided by given Activity.
     *
     * @param componentType Class of component
     * @param o             Object providing the component must be implementing `HasComponent`
     * @param <C>           Type of component
     * @return Component instance
    </C> */
    fun <C> getComponent(componentType: Class<C>, o: Any): C {
        return componentType.cast((o as HasComponent<*>).component)
    }

    fun getActivityComponent(activity: Activity): ActivityComponent {
        return getComponent(ActivityComponent::class.java, activity)
    }

    fun getAppComponent(context: Context): AppComponent {
        return getComponent(AppComponent::class.java, context.applicationContext)
    }
}
