package com.lucasurbas.counter.ui.main

import android.os.Bundle
import com.lucasurbas.counter.R
import com.lucasurbas.counter.app.di.ActivityModule
import com.lucasurbas.counter.app.di.helper.InjectHelper
import com.lucasurbas.counter.ui.BaseActivity
import com.lucasurbas.counter.ui.main.di.MainActivityComponent
import com.lucasurbas.counter.ui.main.di.MainActivityModule
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityComponent>() {

    @Inject
    lateinit var navigator: MainNavigator

    override fun createComponent(): MainActivityComponent {
        return InjectHelper.getAppComponent(this)
                .plus(ActivityModule(this), MainActivityModule())
    }

    override fun onComponentCreated(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigator.navigateToExploreScreen()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navigator.navigateUp()
        return true
    }
}
