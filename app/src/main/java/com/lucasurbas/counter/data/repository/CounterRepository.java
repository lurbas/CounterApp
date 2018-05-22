package com.lucasurbas.counter.data.repository;

import com.lucasurbas.counter.data.model.Counter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface CounterRepository {

    Single<Counter> getCounterWithId(int id);

    Observable<Counter> getCounterUpdatesWithId(int id);

    Single<List<Counter>> getCounters();

    Observable<List<Counter>> getCountersUpdates();

    Completable updateCounter(Counter counter);

}
