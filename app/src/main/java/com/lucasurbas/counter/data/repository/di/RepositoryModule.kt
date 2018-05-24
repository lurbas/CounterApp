package com.lucasurbas.counter.data.repository.di

import com.lucasurbas.counter.data.repository.CounterRepository
import com.lucasurbas.counter.data.repository.MemoryCounterRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideCounterRepository(): CounterRepository {
        val repository = MemoryCounterRepository()
        repository.initData()
        return repository
    }

}
