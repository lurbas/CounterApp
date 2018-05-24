package com.lucasurbas.counter.ui.detail.usecase

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetCounterUpdateInteractor @Inject constructor(
        private val counterRepository: CounterRepository,
        private val interactorSchedulers: InteractorSchedulers) {

    fun getCounterUpdates(id: Int): Observable<Counter> {
        return counterRepository.getCounterUpdatesWithId(id)
                .subscribeOn(interactorSchedulers.backgroundScheduler)
                .observeOn(interactorSchedulers.mainThreadScheduler)
    }
}
