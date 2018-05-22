package com.lucasurbas.counter.app.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.lucasurbas.counter.app.di.vm.ViewModelModule;
import com.lucasurbas.counter.data.repository.di.RepositoryModule;
import com.lucasurbas.counter.remote.di.RemoteModule;
import com.lucasurbas.counter.rx.di.RxModule;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;
import com.lucasurbas.counter.ui.main.di.MainActivityModule;

@Component(
        modules = {
                AppModule.class,
                RxModule.class,
                ViewModelModule.class,
                RepositoryModule.class,
                RemoteModule.class,
        }
)
@Singleton
public interface AppComponent {

    Context getContext();

    MainActivityComponent plus(ActivityModule activityModule, MainActivityModule mainActivityModule);

}
