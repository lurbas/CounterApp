package com.lucasurbas.counter.ui.main

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.lucasurbas.counter.R
import com.lucasurbas.counter.ui.detail.DetailFragment
import com.lucasurbas.counter.ui.explore.ExploreFragment

class MainNavigator(activity: FragmentActivity) {

    private val containerId: Int
    private val fragmentManager: FragmentManager

    init {
        this.containerId = R.id.fragment_container
        this.fragmentManager = activity.supportFragmentManager
    }

    fun navigateToExploreScreen() {
        fragmentManager.beginTransaction()
                .replace(containerId, ExploreFragment.newInstance(), ExploreFragment.TAG)
                .commit()
    }

    fun navigateToDetailScreen(counterId: Int) {
        fragmentManager.beginTransaction()
                .replace(containerId, DetailFragment.newInstance(counterId), DetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
    }

    fun navigateUp() {
        fragmentManager.popBackStack()
    }
}
