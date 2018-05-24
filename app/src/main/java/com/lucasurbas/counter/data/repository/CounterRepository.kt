package com.lucasurbas.counter.data.repository

import com.lucasurbas.counter.data.model.Counter

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface CounterRepository {

    fun getCounterWithId(id: Int): Single<Counter>

    fun getCounterUpdatesWithId(id: Int): Observable<Counter>

    fun getCounters(): Single<List<Counter>>

    fun getCountersUpdates(): Observable<List<Counter>>

    fun updateCounter(counter: Counter): Completable

}
