package com.lucasurbas.counter.ui.explore

import android.arch.lifecycle.ViewModel
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper
import com.lucasurbas.counter.ui.explore.usecase.GetCountersUpdatesInteractor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class ExplorePresenter(
        private val getCountersUpdatesInteractor: GetCountersUpdatesInteractor,
        private val uiCounterItemMapper: UiCounterItemMapper
) : ViewModel() {

    private val stateSubject = BehaviorSubject.create<UiExploreState>()

    private val stateDisposables = CompositeDisposable()
    private val mainDisposable: Disposable

    init {
        val getCountersUpdates = getCountersUpdatesInteractor.getCountersUpdates()
                .flatMap { counterList ->
                    Observable.fromIterable<Counter>(counterList)
                            .map { uiCounterItemMapper.toUiCounterItem(it) }
                            .toList()
                            .toObservable()
                            .map { UiExploreState.Part.CounterList(it) as (UiExploreState.Part) }
                            .onErrorReturn { UiExploreState.Part.Error(it) }
                }

        mainDisposable = getCountersUpdates
                .scan(UiExploreState(), { previousState, changes -> this.viewStateReducer(previousState, changes) })
                .distinctUntilChanged()
                .subscribe { this.nextState(it) }
    }

    private fun viewStateReducer(previousState: UiExploreState, changes: UiExploreState.Part): UiExploreState {
        return changes.computeNewState(previousState)
    }

    private fun nextState(state: UiExploreState) {
        stateSubject.onNext(state)
    }

    fun attachView(view: ExplorePresenter.View) {
        val disposable = stateSubject.subscribe { view.render(it) }
        stateDisposables.add(disposable)
    }

    fun detachView() {
        stateDisposables.clear()
    }

    public override fun onCleared() {
        super.onCleared()
        mainDisposable.dispose()
    }

    interface View {

        fun render(exploreState: UiExploreState)

    }
}
