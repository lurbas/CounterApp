package com.lucasurbas.counter.ui.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.helper.InjectHelper;
import com.lucasurbas.counter.ui.BaseFragment;
import com.lucasurbas.counter.ui.detail.di.DetailFragmentModule;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;

public class DetailFragment extends BaseFragment implements DetailPresenter.View {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String POST_ID_KEY = "post_id_key";

    @Inject
    DetailPresenter presenter;

    @BindView(R.id.web_view)
    WebView webView;

    private Unbinder unbinder;

    public static Fragment newInstance(int postId) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POST_ID_KEY, postId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void injectFragment() {
        InjectHelper.getComponent(MainActivityComponent.class, requireActivity())
                .plus(new FragmentModule(this), new DetailFragmentModule())
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectFragment();
        unbinder = ButterKnife.bind(this, view);
        setupToolbar();

        presenter.attachView(this);
        presenter.getPostIfNeeded(getArguments().getInt(POST_ID_KEY));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    private void setupToolbar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.detail_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void render(UiDetailState uiDetailState) {

        if (uiDetailState.getUrl() != null && !uiDetailState.getUrl().equals(webView.getUrl())) {
            webView.loadUrl(uiDetailState.getUrl());
            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    if (isAdded()) {
                        presenter.markPostAsRead(getArguments().getInt(POST_ID_KEY));
                    }
                }
            });
        }

        if (uiDetailState.getError() != null) {
            Snackbar.make(getView(), R.string.error_general, Snackbar.LENGTH_LONG).show();
        }
    }
}
