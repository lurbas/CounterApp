package com.lucasurbas.counter.data.repository

import com.annimon.stream.Stream
import com.lucasurbas.counter.data.model.Counter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

class MemoryCounterRepository : CounterRepository {

    private val counterList: MutableList<Counter>
    private var counterListSubject: BehaviorSubject<List<Counter>>

    init {
        counterList = ArrayList()
        counterListSubject = BehaviorSubject.create()
    }

    fun initData() {
        counterList.add(Counter(id = 1, value = TimeUnit.SECONDS.toMillis(30), color = "#90CCF4"))
        counterList.add(Counter(id = 2, value = TimeUnit.MINUTES.toMillis(2), color = "#F3D250"))
        counterList.add(Counter(id = 3, value = TimeUnit.MINUTES.toMillis(5), color = "#F78888"))
        counterList.add(Counter(id = 4, value = TimeUnit.MINUTES.toMillis(30), color = "#93C95B"))
        counterListSubject = BehaviorSubject.createDefault(counterList)
    }

    fun initData(list: List<Counter>) {
        counterList.addAll(list)
        counterListSubject = BehaviorSubject.createDefault(counterList)
    }

    override fun getCounterWithId(id: Int): Single<Counter> {
        return Observable.fromIterable(counterList)
                .filter { counter -> counter.isIdEqual(id) }
                .firstOrError()
    }

    override fun getCounterUpdatesWithId(id: Int): Observable<Counter> {
        return counterListSubject
                .flatMap { Observable.fromIterable(it) }
                .filter { counter -> counter.isIdEqual(id) }
    }

    override fun getCounters(): Single<List<Counter>> {
        return Single.fromCallable { counterList }
    }

    override fun getCountersUpdates(): Observable<List<Counter>> {
        return counterListSubject
    }

    override fun updateCounter(counter: Counter): Completable {
        return Completable.fromRunnable {
            val list = Stream.of(counterList)
                    .map { if (it.isIdEqual(counter.id)) counter else it }
                    .toList()
            counterList.clear()
            counterList.addAll(list)
            counterListSubject.onNext(ArrayList(counterList))
        }
    }
}
