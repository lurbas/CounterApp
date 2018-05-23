package com.lucasurbas.counter.ui.detail;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.di.FragmentModule;
import com.lucasurbas.counter.app.di.helper.InjectHelper;
import com.lucasurbas.counter.service.RunningCounterService;
import com.lucasurbas.counter.ui.BaseFragment;
import com.lucasurbas.counter.ui.detail.di.DetailFragmentModule;
import com.lucasurbas.counter.ui.detail.model.UiCounterDetail;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailFragment extends BaseFragment implements DetailPresenter.View {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String COUNTER_ID_KEY = "counter_id_key";

    @BindView(R.id.value_view)
    TextView valueView;
    @BindView(R.id.start_button)
    Button startButton;
    @BindView(R.id.stop_button)
    Button stopButton;

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
        UiCounterDetail counter = uiDetailState.getCounter();
        if (counter != null) {
            startButton.setVisibility(counter.getIsRunning() ? GONE : VISIBLE);
            stopButton.setVisibility(counter.getIsRunning() ? VISIBLE : GONE);
            valueView.setText(uiDetailState.getCounter().getStringValue());
        }

        if (uiDetailState.getError() != null) {
            Snackbar.make(getView(), R.string.error_general, Snackbar.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.start_button)
    void onStartClick() {
        Intent intent = RunningCounterService.newInstance(getActivity(),
                RunningCounterService.ACTION_START, getArguments().getInt(COUNTER_ID_KEY));
        getActivity().startService(intent);
    }

    @OnClick(R.id.stop_button)
    void onStopClick() {
        Intent intent = RunningCounterService.newInstance(getActivity(),
                RunningCounterService.ACTION_STOP, getArguments().getInt(COUNTER_ID_KEY));
        getActivity().startService(intent);
    }
}
