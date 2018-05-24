package com.lucasurbas.counter.ui.detail

import android.arch.lifecycle.ViewModel
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.service.usecase.GetCounterInteractor
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor
import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class DetailPresenter(
        private val getCounterUpdateInteractor: GetCounterUpdateInteractor,
        private val getCounterInteractor: GetCounterInteractor,
        private val updateCounterInteractor: UpdateCounterInteractor,
        private val uiCounterDetailMapper: UiCounterDetailMapper
) : ViewModel() {

    private val getCounterActionSubject = PublishSubject.create<Int>()
    private val updateCounterActionSubject = PublishSubject.create<Counter>()
    private val stateSubject = BehaviorSubject.create<UiDetailState>()

    private val stateDisposables = CompositeDisposable()
    private val mainDisposable: Disposable

    init {
        val getCounterUpdates = getCounterActionSubject
                .flatMap { counterId ->
                    getCounterUpdateInteractor.getCounterUpdates(counterId!!)
                            .map { uiCounterDetailMapper.toUiCounterDetail(it) }
                            .map { UiDetailState.Part.CounterItem(it) as UiDetailState.Part }
                            .onErrorReturn { UiDetailState.Part.Error(it) }
                }

        val updateCounter = updateCounterActionSubject
                .flatMap { counter ->
                    updateCounterInteractor.updateCounter(counter)
                            .andThen(Observable.fromCallable { UiDetailState.Part.CounterUpdated() as UiDetailState.Part })
                            .onErrorReturn { UiDetailState.Part.Error(it) }
                }

        mainDisposable = Observable.merge(getCounterUpdates, updateCounter)
                .scan(UiDetailState(), { previousState, changes -> this.viewStateReducer(previousState, changes) })
                .distinctUntilChanged()
                .subscribe { stateSubject.onNext(it) }
    }

    private fun viewStateReducer(previousState: UiDetailState, changes: UiDetailState.Part): UiDetailState {
        return changes.computeNewState(previousState)
    }

    fun attachView(view: DetailPresenter.View) {
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

    fun getCounter(counterId: Int) {
        getCounterActionSubject.onNext(counterId)
    }

    fun updateInitialValue(counterId: Int, newValue: Long) {
        val disposable = getCounterInteractor.getCounter(counterId)
                .subscribe { counter ->
                    updateCounterActionSubject.onNext(counter.copy(value = newValue))
                }
        stateDisposables.add(disposable)
    }

    interface View {

        fun render(detailState: UiDetailState)

    }
}
