package com.lucasurbas.counter.service.di;

import android.app.NotificationManager;
import android.content.Context;

import com.lucasurbas.counter.app.di.scope.ServiceScope;
import com.lucasurbas.counter.service.AndroidTimers;
import com.lucasurbas.counter.service.RunningCounterPresenter;
import com.lucasurbas.counter.service.Timers;
import com.lucasurbas.counter.service.usecase.GetCounterInteractor;
import com.lucasurbas.counter.service.usecase.GetRunningCountersUpdatesInteractor;
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor;
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper;

import dagger.Module;
import dagger.Provides;

@Module
public class RunningCounterServiceModule {

    @Provides
    Timers provideTimers() {
        return new AndroidTimers();
    }

    @Provides
    @ServiceScope
    RunningCounterPresenter provideCounterServicePresenter(
            GetRunningCountersUpdatesInteractor getRunningCountersUpdatesInteractor,
            GetCounterInteractor getCounterInteractor,
            UpdateCounterInteractor updateCounterInteractor,
            UiCounterItemMapper uiMapper,
            Timers timers) {
        return new RunningCounterPresenter(getRunningCountersUpdatesInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiMapper,
                timers);
    }

    @Provides
    NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
