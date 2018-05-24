package com.lucasurbas.counter.ui.detail.widget

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject


class CounterViewPresenter {

    private val updateValueActionSubject = PublishSubject.create<Long>()
    var mainDisposable: Disposable? = null;


    fun start(view: CounterViewPresenter.View) {

        val updateValue = updateValueActionSubject
                .map { UiCounterViewState.Part.CounterValue(it) }

        mainDisposable = updateValue
                .scan(UiCounterViewState(), { previousState, changes -> this.viewStateReducer(previousState, changes) })
                .distinctUntilChanged()
                .subscribe { view.render(it) }
    }

    private fun viewStateReducer(previousState: UiCounterViewState, changes: UiCounterViewState.Part): UiCounterViewState {
        return changes.computeNewState(previousState)
    }

    fun destroy() {
        mainDisposable?.dispose()
    }

    fun newValue(value: Long) {
        updateValueActionSubject.onNext(value)
    }


    interface View {

        fun render(counterViewState: UiCounterViewState)

    }
}
