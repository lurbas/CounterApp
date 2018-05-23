package com.lucasurbas.counter.service.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetCounterInteractor {

    private final CounterRepository counterRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public GetCounterInteractor(CounterRepository counterRepository,
                                               InteractorSchedulers interactorSchedulers){
        this.counterRepository = counterRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Single<Counter> getCounter(int counterId){
        return counterRepository.getCounterWithId(counterId)
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
