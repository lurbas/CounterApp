package com.lucasurbas.counter.data.repository.di;

import com.lucasurbas.counter.data.repository.CounterRepository;
import com.lucasurbas.counter.data.repository.MemoryCounterRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    CounterRepository provideCounterRepository() {
        MemoryCounterRepository repository = new MemoryCounterRepository();
        repository.initData();
        return repository;
    }

}
