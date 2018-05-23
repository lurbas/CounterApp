package com.lucasurbas.counter.ui.explore;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;
import com.lucasurbas.counter.ui.explore.usecase.GetCountersUpdatesInteractor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ExplorePresenter extends ViewModel {

    private final GetCountersUpdatesInteractor getCountersUpdatesInteractor;
    private final UiCounterItemMapper uiCounterItemMapper;

    private final PublishSubject<String> createCounterActionSubject = PublishSubject.create();
    private final BehaviorSubject<UiExploreState> stateSubject = BehaviorSubject.create();

    private final CompositeDisposable stateDisposables = new CompositeDisposable();
    private Disposable mainDisposable;

    public ExplorePresenter(GetCountersUpdatesInteractor getCountersUpdatesInteractor,
                            UiCounterItemMapper uiCounterItemMapper) {
        this.getCountersUpdatesInteractor = getCountersUpdatesInteractor;
        this.uiCounterItemMapper = uiCounterItemMapper;

        init();
    }

    private void init() {
        Observable<UiExploreState.Part> loadCountersAction = Observable.empty();

        Observable<UiExploreState.Part> getCountersUpdates = getCountersUpdatesInteractor.getCountersUpdates()
                .flatMap(counterList -> Observable.fromIterable(counterList)
                        .map(uiCounterItemMapper::toUiCounterItem)
                        .toList()
                        .toObservable()
                        .map((Function<List<UiCounterItem>, UiExploreState.Part>) UiExploreState.Part.CounterList::new)
                        .onErrorReturn(UiExploreState.Part.Error::new));

//        Flowable<UiExploreState.Part> getCountersUpdates = getCountersUpdatesInteractor.getCountersUpdates()
//                .flatMap(counterList -> Flowable.fromIterable(counterList)
//                        .map(uiCounterItemMapper::toUiCounterItem)
//                        .toList()
//                        .map((Function<List<UiCounterItem>, UiExploreState.Part>) UiExploreState.Part.CounterList::new)
//                        .onErrorReturn(UiExploreState.Part.Error::new);

        mainDisposable = Observable.merge(loadCountersAction, getCountersUpdates)
                .scan(UiExploreState.initialState(), this::viewStateReducer)
                .distinctUntilChanged()
                .subscribe(this::nextState);
    }

    private UiExploreState viewStateReducer(UiExploreState previousState, UiExploreState.Part changes) {
        return changes.computeNewState(previousState);
    }

    private void nextState(UiExploreState state) {
        Log.v("TAG_ExplorePresenter", state.toString());
        stateSubject.onNext(state);
    }

    public void attachView(ExplorePresenter.View view) {
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

    public void createCounter() {
        createCounterActionSubject.onNext("");
    }

    public interface View {

        void render(UiExploreState exploreState);

    }
}
