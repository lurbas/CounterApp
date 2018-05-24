package com.lucasurbas.counter.ui.explore.usecase

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetCountersUpdatesInteractor @Inject constructor(
        private val counterRepository: CounterRepository,
        private val interactorSchedulers: InteractorSchedulers) {

    fun getCountersUpdates(): Observable<List<Counter>> {
        return counterRepository.countersUpdates
                .subscribeOn(interactorSchedulers.backgroundScheduler)
                .observeOn(interactorSchedulers.mainThreadScheduler)
    }
}
