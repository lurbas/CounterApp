package com.lucasurbas.counter.data.repository;

import com.annimon.stream.Stream;
import com.lucasurbas.counter.data.model.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class MemoryCounterRepository implements CounterRepository {

    private final List<Counter> counterList;
    private BehaviorSubject<List<Counter>> counterListSubject;

    public MemoryCounterRepository() {
        counterList = new ArrayList<>();
        counterListSubject = BehaviorSubject.create();
    }

    public void initData(){
        counterList.add(Counter.builder()
                .id(1)
                .value(TimeUnit.SECONDS.toMillis(30))
                .build());
        counterList.add(Counter.builder()
                .id(2)
                .value(TimeUnit.MINUTES.toMillis(2))
                .build());
        counterList.add(Counter.builder()
                .id(3)
                .value(TimeUnit.MINUTES.toMillis(5))
                .build());
        counterList.add(Counter.builder()
                .id(4)
                .value(TimeUnit.MINUTES.toMillis(30))
                .build());
        counterListSubject = BehaviorSubject.createDefault(counterList);
    }

    public void initData(List<Counter> list){
        counterList.addAll(list);
        counterListSubject = BehaviorSubject.createDefault(counterList);
    }

    @Override
    public Single<Counter> getCounterWithId(int id) {
        return Observable.fromIterable(counterList)
                .filter(counter -> counter.isIdEqual(id))
                .firstOrError();
    }

    @Override
    public Observable<Counter> getCounterUpdatesWithId(int id) {
        return counterListSubject
                .flatMap(Observable::fromIterable)
                .filter(counter -> counter.isIdEqual(id));
    }

    @Override
    public Single<List<Counter>> getCounters() {
        return Single.fromCallable(() -> counterList);
    }

    @Override
    public Observable<List<Counter>> getCountersUpdates() {
        return counterListSubject;
    }

    @Override
    public Completable updateCounter(final Counter updatedCounter) {
        return Completable.fromRunnable(() -> {
            List<Counter> list = Stream.of(counterList)
                    .map(counter -> counter.isIdEqual(updatedCounter.getId()) ? updatedCounter : counter)
                    .toList();
            counterList.clear();
            counterList.addAll(list);
            counterListSubject.onNext(new ArrayList<>(counterList));
        });
    }
}
