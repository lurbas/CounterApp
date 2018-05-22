package com.lucasurbas.counter.ui.explore.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCountersUpdatesInteractor {

    private final CounterRepository counterRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public GetCountersUpdatesInteractor(CounterRepository counterRepository,
                                        InteractorSchedulers interactorSchedulers){
        this.counterRepository = counterRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Observable<List<Counter>> getCountersUpdates(){
        return counterRepository.getCountersUpdates()
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
