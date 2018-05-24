package com.lucasurbas.counter.ui.main

import android.os.Build
import android.support.transition.ChangeBounds
import android.support.transition.ChangeTransform
import android.support.transition.Fade
import android.support.transition.TransitionSet
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View
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

    fun navigateToDetailScreen(counterId: Int, sharedElement: View, previousFragment: Fragment) {

        val nextFragment = DetailFragment.newInstance(counterId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // 1. Exit for Previous Fragment
            val exitFade = Fade()
            previousFragment.setExitTransition(exitFade)

            // 2. Shared Elements TransitionSet
            val enterTransitionSet = TransitionSet()
            enterTransitionSet.addTransition(ChangeBounds())
            enterTransitionSet.addTransition(ChangeTransform())
            nextFragment.setSharedElementEnterTransition(enterTransitionSet)

            // 3. Enter Transition for New Fragment
            val enterFade = Fade()
            enterFade.startDelay = 300
            nextFragment.setEnterTransition(enterFade)

            // 4. Shared Elements Transition
            val exitTransitionSet = TransitionSet()
            exitTransitionSet.addTransition(ChangeBounds())
            exitTransitionSet.addTransition(ChangeTransform())
            nextFragment.setSharedElementReturnTransition(exitTransitionSet)
        }

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(sharedElement, "backgroundImage$counterId")
                .replace(containerId, nextFragment, DetailFragment.TAG)
                .addToBackStack(null)
                .commit()
    }

    fun navigateUp() {
        fragmentManager.popBackStack()
    }
}
