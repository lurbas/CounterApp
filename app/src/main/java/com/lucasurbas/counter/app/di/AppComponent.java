package com.lucasurbas.counter.app.di;

import android.content.Context;

import com.lucasurbas.counter.app.di.rx.RxModule;
import com.lucasurbas.counter.app.di.vm.ViewModelModule;
import com.lucasurbas.counter.data.repository.di.RepositoryModule;
import com.lucasurbas.counter.ui.main.di.MainActivityComponent;
import com.lucasurbas.counter.ui.main.di.MainActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                AppModule.class,
                RxModule.class,
                ViewModelModule.class,
                RepositoryModule.class,
        }
)
@Singleton
public interface AppComponent {

    Context getContext();

    MainActivityComponent plus(ActivityModule activityModule, MainActivityModule mainActivityModule);

}
