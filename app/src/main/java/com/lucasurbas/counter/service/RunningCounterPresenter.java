package com.lucasurbas.counter.service;

import android.os.CountDownTimer;

import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.service.usecase.GetCounterInteractor;
import com.lucasurbas.counter.service.usecase.GetRunningCountersUpdatesInteractor;
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor;
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class RunningCounterPresenter {

    private final GetRunningCountersUpdatesInteractor getRunningCountersUpdatesInteractor;
    private final GetCounterInteractor getCounterInteractor;
    private final UpdateCounterInteractor updateCounterInteractor;
    private final UiCounterItemMapper uiCounterItemMapper;

    private final PublishSubject<Counter> updatedCounterActionSubject = PublishSubject.create();
    private final BehaviorSubject<UiRunningCounterState> stateSubject = BehaviorSubject.create();

    private final CompositeDisposable stateDisposables = new CompositeDisposable();
    private Disposable mainDisposable;

    private Map<Integer, CountDownTimer> timerMap = new HashMap<>();

    public RunningCounterPresenter(GetRunningCountersUpdatesInteractor getRunningCountersUpdatesInteractor,
                                   GetCounterInteractor getCounterInteractor,
                                   UpdateCounterInteractor updateCounterInteractor,
                                   UiCounterItemMapper uiCounterItemMapper) {
        this.getRunningCountersUpdatesInteractor = getRunningCountersUpdatesInteractor;
        this.getCounterInteractor = getCounterInteractor;
        this.updateCounterInteractor = updateCounterInteractor;
        this.uiCounterItemMapper = uiCounterItemMapper;

        init();
    }

    private void init() {
        Observable<UiRunningCounterState.Part> getCountersUpdates = getRunningCountersUpdatesInteractor.getRunningCountersUpdates()
                .flatMap(counterList -> Observable.fromIterable(counterList)
                        .map(uiCounterItemMapper::toUiCounterItem)
                        .toList()
                        .toObservable()
                        .map((Function<List<UiCounterItem>, UiRunningCounterState.Part>) UiRunningCounterState.Part.RunningCounterList::new)
                        .onErrorReturn(UiRunningCounterState.Part.Error::new));

        Observable<UiRunningCounterState.Part> updateCounter = updatedCounterActionSubject
                .flatMap(counter -> updateCounterInteractor.updateCounter(counter)
                        .andThen(Observable.fromCallable((Callable<UiRunningCounterState.Part>) UiRunningCounterState.Part.CounterUpdated::new))
                        .onErrorReturn(UiRunningCounterState.Part.Error::new));

        mainDisposable = Observable.merge(getCountersUpdates, updateCounter)
                .scan(UiRunningCounterState.initialState(), this::viewStateReducer)
                .distinctUntilChanged()
                .subscribe(stateSubject::onNext);
    }

    private UiRunningCounterState viewStateReducer(UiRunningCounterState previousState, UiRunningCounterState.Part changes) {
        return changes.computeNewState(previousState);
    }

    public void attachView(View view) {
        Disposable disposable = stateSubject.subscribe(view::render);
        stateDisposables.add(disposable);
    }

    public void detachView() {
        stateDisposables.clear();
    }

    public void destroy() {
        mainDisposable.dispose();
    }

    public void startCounter(final int counterId) {
        CountDownTimer timerInMap = timerMap.get(counterId);
        if (timerInMap != null) {
            return;
        }
        Disposable disposable = getCounterInteractor.getCounter(counterId)
                .subscribe(counter -> {
                    CountDownTimer timer = new CountDownTimer(20000, 51) {

                        public void onTick(long millisUntilFinished) {
                            Counter newCounter = counter.toBuilder()
                                    .value(millisUntilFinished)
                                    .isRunning(true)
                                    .build();
                            updatedCounterActionSubject.onNext(newCounter);
                        }

                        public void onFinish() {
                            Counter newCounter = counter.toBuilder()
                                    .value(0)
                                    .isRunning(false)
                                    .build();
                            updatedCounterActionSubject.onNext(newCounter);
                            timerMap.remove(counterId);
                        }

                    };
                    timerMap.put(counterId, timer);
                    timer.start();
                });
        stateDisposables.add(disposable);
    }

    public void stopCounter(final int counterId) {
        CountDownTimer timerInMap = timerMap.get(counterId);
        if (timerInMap == null) {
            return;
        }
        Disposable disposable = getCounterInteractor.getCounter(counterId)
                .subscribe(counter -> {
                    timerInMap.cancel();

                    Counter newCounter = counter.toBuilder()
                            .isRunning(false)
                            .build();
                    updatedCounterActionSubject.onNext(newCounter);
                    timerMap.remove(counterId);
                });
        stateDisposables.add(disposable);
    }

    public interface View {

        void render(UiRunningCounterState runningCounterState);

    }
}
