package com.lucasurbas.counter.service.usecase

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers
import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateCounterInteractor @Inject constructor(
        private val counterRepository: CounterRepository,
        private val interactorSchedulers: InteractorSchedulers) {

    fun updateCounter(counter: Counter): Completable {
        return counterRepository.updateCounter(counter)
                .subscribeOn(interactorSchedulers.singleScheduler)
                .observeOn(interactorSchedulers.mainThreadScheduler)
    }
}
