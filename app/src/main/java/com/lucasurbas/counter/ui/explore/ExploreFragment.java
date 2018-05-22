package com.lucasurbas.counter.ui.explore;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.helper.InjectHelper;
import com.lucasurbas.counter.ui.BaseFragment;
import com.lucasurbas.counter.ui.explore.di.ExploreFragmentModule;
import com.lucasurbas.counter.ui.main.MainNavigator;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;
import com.lucasurbas.counter.utils.SpaceItemDecoration;

public class ExploreFragment extends BaseFragment implements ExplorePresenter.View {

    public static final String TAG = ExploreFragment.class.getSimpleName();
    private static final String USER_ID = "32192";
    private static final int COLUMNS_COUNT = 3;

    @Inject
    ExplorePresenter presenter;
    @Inject
    MainNavigator navigator;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.explore_recycler_view)
    RecyclerView exploreRecyclerView;
    @BindView(R.id.error_layout)
    View errorLayout;
    @BindView(R.id.retry_button)
    Button retryButton;

    private ExploreAdapter exploreAdapter;
    private Unbinder unbinder;

    public static Fragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void injectFragment() {
        InjectHelper.getComponent(MainActivityComponent.class, requireActivity())
                .plus(new FragmentModule(this), new ExploreFragmentModule())
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectFragment();
        unbinder = ButterKnife.bind(this, view);
        setupToolbar();
        setupView();

        presenter.attachView(this);
        presenter.loadPostsIfNeeded(USER_ID);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    private void setupToolbar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.explore_title);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    private void setupView() {
        exploreAdapter = new ExploreAdapter(this::navigateToPostDetailScreen);
        exploreAdapter.setHasStableIds(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), COLUMNS_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return exploreAdapter.getSpanSize(position, gridLayoutManager.getSpanCount());
            }
        });
        exploreRecyclerView.setLayoutManager(gridLayoutManager);
        exploreRecyclerView.setAdapter(exploreAdapter);
        int tileMargin = getResources().getDimensionPixelOffset(R.dimen.tile_margin);
        exploreRecyclerView.addItemDecoration(new SpaceItemDecoration(tileMargin));
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadPosts(USER_ID));
        retryButton.setOnClickListener(view -> presenter.loadPosts(USER_ID));
    }

    private void navigateToPostDetailScreen(int postId) {
        navigator.navigateToDetailScreen(postId);
    }

    @Override
    public void render(UiExploreState uiExploreState) {

        exploreAdapter.updateItemList(uiExploreState.getItemList());
        swipeRefreshLayout.setRefreshing(uiExploreState.getIsLoading());

        errorLayout.setVisibility(uiExploreState.getItemList().isEmpty()
                && uiExploreState.getError() != null ? VISIBLE : GONE);
        if (uiExploreState.getError() != null){
            Snackbar.make(getView(), R.string.error_general, Snackbar.LENGTH_LONG).show();
        }
    }
}
