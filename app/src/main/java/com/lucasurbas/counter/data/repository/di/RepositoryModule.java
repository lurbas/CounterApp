package com.lucasurbas.counter.data.repository.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.lucasurbas.counter.data.repository.MemoryPostRepository;
import com.lucasurbas.counter.data.repository.PostRepository;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    PostRepository providePostRepository() {
        return new MemoryPostRepository();
    }

}
