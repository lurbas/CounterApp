package com.lucasurbas.counter.service

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.service.usecase.GetCounterInteractor
import com.lucasurbas.counter.service.usecase.GetRunningCountersUpdatesInteractor
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class RunningCounterPresenter(
        private val getRunningCountersUpdatesInteractor: GetRunningCountersUpdatesInteractor,
        private val getCounterInteractor: GetCounterInteractor,
        private val updateCounterInteractor: UpdateCounterInteractor,
        private val uiCounterItemMapper: UiCounterItemMapper,
        private val timers: Timers
) {

    private val updateCounterActionSubject = PublishSubject.create<Counter>()
    private val stateSubject = BehaviorSubject.create<UiRunningCounterState>()

    private val stateDisposables = CompositeDisposable()
    private val mainDisposable: Disposable

    init {
        val getCountersUpdates = getRunningCountersUpdatesInteractor.getRunningCountersUpdates()
                .flatMap { counterList ->
                    Observable.fromIterable(counterList)
                            .map { uiCounterItemMapper.toUiCounterItem(it) }
                            .toList()
                            .toObservable()
                            .map { UiRunningCounterState.Part.RunningCounterList(it) as UiRunningCounterState.Part }
                            .onErrorReturn { UiRunningCounterState.Part.Error(it) }
                }

        val updateCounter = updateCounterActionSubject
                .flatMap { counter ->
                    updateCounterInteractor.updateCounter(counter)
                            .andThen(Observable.fromCallable { UiRunningCounterState.Part.CounterUpdated() as UiRunningCounterState.Part })
                            .onErrorReturn { UiRunningCounterState.Part.Error(it) }
                }

        mainDisposable = Observable.merge(getCountersUpdates, updateCounter)
                .scan(UiRunningCounterState(), { previousState, changes -> this.viewStateReducer(previousState, changes) })
                .distinctUntilChanged()
                .subscribe { stateSubject.onNext(it) }
    }

    private fun viewStateReducer(previousState: UiRunningCounterState, changes: UiRunningCounterState.Part): UiRunningCounterState {
        return changes.computeNewState(previousState)
    }

    fun attachView(view: View) {
        val disposable = stateSubject.subscribe { view.render(it) }
        stateDisposables.add(disposable)
    }

    fun detachView() {
        stateDisposables.clear()
    }

    fun destroy() {
        mainDisposable.dispose()
    }

    fun startCounter(counterId: Int) {
        if (timers.hasTimer(counterId)) {
            return
        }
        val disposable = getCounterInteractor.getCounter(counterId)
                .subscribe { counter ->
                    timers.startNew(counterId, counter.value, object : Timers.TimerListener {

                        override fun onTick(millisUntilFinished: Long) {
                            val newCounter = counter.copy(value = millisUntilFinished, isRunning = true)
                            updateCounterActionSubject.onNext(newCounter)
                        }

                        override fun onFinish() {
                            val newCounter = counter.copy(value = 0, isRunning = false)
                            updateCounterActionSubject.onNext(newCounter)
                            timers.remove(counterId)
                        }
                    })
                }
        stateDisposables.add(disposable)
    }

    fun stopCounter(counterId: Int) {
        if (!timers.hasTimer(counterId)) {
            return
        }
        val disposable = getCounterInteractor.getCounter(counterId)
                .subscribe { counter ->
                    val newCounter = counter.copy(isRunning = false)
                    updateCounterActionSubject.onNext(newCounter)
                    timers.remove(counterId)
                }
        stateDisposables.add(disposable)
    }

    interface View {

        fun render(runningCounterState: UiRunningCounterState)

    }
}
