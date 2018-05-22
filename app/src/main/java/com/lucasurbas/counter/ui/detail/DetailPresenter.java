package com.lucasurbas.counter.ui.detail;

import android.arch.lifecycle.ViewModel;

import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper;
import com.lucasurbas.counter.ui.detail.model.UiCounterDetail;
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class DetailPresenter extends ViewModel {

    private final GetCounterUpdateInteractor getCounterUpdateInteractor;
    private final UiCounterDetailMapper uiCounterDetailMapper;

    private final PublishSubject<Integer> getCounterActionSubject = PublishSubject.create();
    private final BehaviorSubject<UiDetailState> stateSubject = BehaviorSubject.create();

    private final CompositeDisposable stateDisposables = new CompositeDisposable();
    private Disposable mainDisposable;

    public DetailPresenter(GetCounterUpdateInteractor getCounterUpdateInteractor,
                           UiCounterDetailMapper uiCounterDetailMapper) {
        this.getCounterUpdateInteractor = getCounterUpdateInteractor;
        this.uiCounterDetailMapper = uiCounterDetailMapper;

        init();
    }

    private void init() {
        Observable<UiDetailState.Part> getCounterUpdates = getCounterActionSubject
                .flatMap(counterId -> getCounterUpdateInteractor.getCounterUpdates(counterId)
                        .map(uiCounterDetailMapper::toUiCounterDetail)
                        .map((Function<UiCounterDetail, UiDetailState.Part>) UiDetailState.Part.CounterItem::new)
                        .onErrorReturn(UiDetailState.Part.Error::new));

        mainDisposable = getCounterUpdates
                .scan(UiDetailState.initialState(), this::viewStateReducer)
                .distinctUntilChanged()
                .subscribe(stateSubject::onNext);
    }

    private UiDetailState viewStateReducer(UiDetailState previousState, UiDetailState.Part changes) {
        return changes.computeNewState(previousState);
    }

    public void attachView(DetailPresenter.View view) {
        Disposable disposable = stateSubject.subscribe(view::render);
        stateDisposables.add(disposable);
    }

    public void detachView() {
        stateDisposables.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mainDisposable.dispose();
    }

    public void getCounter(final int id) {
        getCounterActionSubject.onNext(id);
    }

    public interface View {

        void render(UiDetailState exploreState);

    }
}
