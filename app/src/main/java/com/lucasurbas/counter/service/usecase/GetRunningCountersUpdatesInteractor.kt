package com.lucasurbas.counter.service.usecase

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetRunningCountersUpdatesInteractor @Inject constructor(
        private val counterRepository: CounterRepository,
        private val interactorSchedulers: InteractorSchedulers) {

    fun getRunningCountersUpdates(): Observable<List<Counter>> {
        return counterRepository.getCountersUpdates()
                .flatMap { counters ->
                    Observable.fromIterable(counters)
                            .filter { it.isRunning }
                            .toList()
                            .toObservable()
                }
                .subscribeOn(interactorSchedulers.backgroundScheduler)
                .observeOn(interactorSchedulers.mainThreadScheduler)
    }
}
