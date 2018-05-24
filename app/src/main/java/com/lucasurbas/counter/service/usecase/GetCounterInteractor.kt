package com.lucasurbas.counter.service.usecase

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCounterInteractor @Inject constructor(
        private val counterRepository: CounterRepository,
        private val interactorSchedulers: InteractorSchedulers) {

    fun getCounter(counterId: Int): Single<Counter> {
        return counterRepository.getCounterWithId(counterId)
                .subscribeOn(interactorSchedulers.backgroundScheduler)
                .observeOn(interactorSchedulers.mainThreadScheduler)
    }
}
