package com.lucasurbas.counter.ui.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lucasurbas.counter.R;
import com.lucasurbas.counter.ui.BaseActivity;
import com.lucasurbas.counter.ui.detail.DetailFragment;
import com.lucasurbas.counter.ui.explore.ExploreFragment;

public class MainNavigator {

    private final int containerId;
    private final BaseActivity activity;
    private final FragmentManager fragmentManager;

    public MainNavigator(BaseActivity activity) {
        this.containerId = R.id.fragment_container;
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public void navigateToExploreScreen() {
        fragmentManager.beginTransaction()
                .replace(containerId, ExploreFragment.newInstance(), ExploreFragment.TAG)
                .commit();
    }

    public void navigateToDetailScreen(final int counterId) {
        fragmentManager.beginTransaction()
                .replace(containerId, DetailFragment.newInstance(counterId), DetailFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    public void navigateUp() {
        fragmentManager.popBackStack();
    }
}
