package com.lucasurbas.counter.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.lucasurbas.counter.app.di.helper.HasComponent

abstract class BaseActivity<C> : AppCompatActivity(), HasComponent<C> {

    override var component: C? = null

    /**
     * Is called after the component has been created.
     * Must be implemented to call the injection on the object, e.g. `component.inject(this)`.
     *
     * @param component Component which should be used to inject dependencies
     */
    protected open fun onComponentCreated(component: C) {}

    /**
     * Creates component instance.
     */
    protected abstract fun createComponent(): C

    override fun onCreate(savedInstanceState: Bundle?) {
        component = createComponent()
        if (component == null) {
            throw NullPointerException("Component must not be null")
        }
        super.onCreate(savedInstanceState)
        onComponentCreated(component!!)
    }
}
