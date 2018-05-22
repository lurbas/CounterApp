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
import android.widget.TextView;

import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.helper.InjectHelper;
import com.lucasurbas.counter.ui.BaseFragment;
import com.lucasurbas.counter.ui.detail.di.DetailFragmentModule;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailFragment extends BaseFragment implements DetailPresenter.View {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String COUNTER_ID_KEY = "counter_id_key";

    @BindView(R.id.value_view)
    TextView valueView;

    @Inject
    DetailPresenter presenter;

    private Unbinder unbinder;

    public static Fragment newInstance(int counterId) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COUNTER_ID_KEY, counterId);
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
        presenter.getCounter(getArguments().getInt(COUNTER_ID_KEY));
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

        if (uiDetailState.getCounter() != null) {
            valueView.setText(uiDetailState.getCounter().getStringValue());
        }

        if (uiDetailState.getError() != null) {
            Snackbar.make(getView(), R.string.error_general, Snackbar.LENGTH_LONG).show();
        }
    }
}
