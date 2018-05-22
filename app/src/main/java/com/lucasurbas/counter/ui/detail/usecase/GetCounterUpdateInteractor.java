package com.lucasurbas.counter.ui.detail.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCounterUpdateInteractor {

    private final CounterRepository counterRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public GetCounterUpdateInteractor(CounterRepository counterRepository,
                                      InteractorSchedulers interactorSchedulers) {
        this.counterRepository = counterRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Observable<Counter> getCounterUpdates(int id) {
        return counterRepository.getCounterUpdatesWithId(id)
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
