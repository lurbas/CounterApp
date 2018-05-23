package com.lucasurbas.counter.service.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetRunningCountersUpdatesInteractor {

    private final CounterRepository counterRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public GetRunningCountersUpdatesInteractor(CounterRepository counterRepository,
                                               InteractorSchedulers interactorSchedulers) {
        this.counterRepository = counterRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Observable<List<Counter>> getRunningCountersUpdates() {
        return counterRepository.getCountersUpdates()
                .flatMap(counters -> Observable.fromIterable(counters)
                        .filter(Counter::getIsRunning)
                        .toList()
                        .toObservable())
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
