package com.lucasurbas.counter.service.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateCounterInteractor {

    private final CounterRepository counterRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public UpdateCounterInteractor(CounterRepository counterRepository,
                                InteractorSchedulers interactorSchedulers){
        this.counterRepository = counterRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Completable updateCounter(Counter counter){
        return counterRepository.updateCounter(counter)
                .subscribeOn(interactorSchedulers.getSingleScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
