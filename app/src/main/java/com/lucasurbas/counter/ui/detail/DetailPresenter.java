package com.lucasurbas.counter.ui.detail;

import android.arch.lifecycle.ViewModel;

import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.service.usecase.GetCounterInteractor;
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor;
import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper;
import com.lucasurbas.counter.ui.detail.model.UiCounterDetail;
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class DetailPresenter extends ViewModel {

    private final GetCounterUpdateInteractor getCounterUpdateInteractor;
    private final GetCounterInteractor getCounterInteractor;
    private final UpdateCounterInteractor updateCounterInteractor;
    private final UiCounterDetailMapper uiCounterDetailMapper;

    private final PublishSubject<Integer> getCounterActionSubject = PublishSubject.create();
    private final PublishSubject<Counter> updateCounterActionSubject = PublishSubject.create();
    private final BehaviorSubject<UiDetailState> stateSubject = BehaviorSubject.create();

    private final CompositeDisposable stateDisposables = new CompositeDisposable();
    private Disposable mainDisposable;

    public DetailPresenter(GetCounterUpdateInteractor getCounterUpdateInteractor,
                           GetCounterInteractor getCounterInteractor,
                           UpdateCounterInteractor updateCounterInteractor,
                           UiCounterDetailMapper uiCounterDetailMapper) {
        this.getCounterUpdateInteractor = getCounterUpdateInteractor;
        this.getCounterInteractor = getCounterInteractor;
        this.updateCounterInteractor = updateCounterInteractor;
        this.uiCounterDetailMapper = uiCounterDetailMapper;

        init();
    }

    private void init() {
        Observable<UiDetailState.Part> getCounterUpdates = getCounterActionSubject
                .flatMap(counterId -> getCounterUpdateInteractor.getCounterUpdates(counterId)
                        .map(uiCounterDetailMapper::toUiCounterDetail)
                        .map((Function<UiCounterDetail, UiDetailState.Part>) UiDetailState.Part.CounterItem::new)
                        .onErrorReturn(UiDetailState.Part.Error::new));

        Observable<UiDetailState.Part> updateCounter = updateCounterActionSubject
                .flatMap(counter -> updateCounterInteractor.updateCounter(counter)
                        .andThen(Observable.fromCallable((Callable<UiDetailState.Part>) UiDetailState.Part.CounterUpdated::new))
                        .onErrorReturn(UiDetailState.Part.Error::new));

        mainDisposable = Observable.merge(getCounterUpdates, updateCounter)
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

    public void getCounter(final int counterId) {
        getCounterActionSubject.onNext(counterId);
    }

    public void updateInitialValue(int counterId, long newValue) {
        Disposable disposable = getCounterInteractor.getCounter(counterId)
                .subscribe(counter -> {
                    Counter newCounter = counter.toBuilder()
                            .value(newValue)
                            .build();
                    updateCounterActionSubject.onNext(newCounter);
                });
        stateDisposables.add(disposable);
    }

    public interface View {

        void render(UiDetailState detailState);

    }
}
