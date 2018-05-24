package com.lucasurbas.counter.service.di

import android.app.NotificationManager
import android.content.Context

import com.lucasurbas.counter.app.di.scope.ServiceScope
import com.lucasurbas.counter.service.AndroidTimers
import com.lucasurbas.counter.service.RunningCounterPresenter
import com.lucasurbas.counter.service.Timers
import com.lucasurbas.counter.service.usecase.GetCounterInteractor
import com.lucasurbas.counter.service.usecase.GetRunningCountersUpdatesInteractor
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper

import dagger.Module
import dagger.Provides

@Module
class RunningCounterServiceModule {

    @Provides
    internal fun provideTimers(): Timers {
        return AndroidTimers()
    }

    @Provides
    @ServiceScope
    internal fun provideCounterServicePresenter(
            getRunningCountersUpdatesInteractor: GetRunningCountersUpdatesInteractor,
            getCounterInteractor: GetCounterInteractor,
            updateCounterInteractor: UpdateCounterInteractor,
            uiMapper: UiCounterItemMapper,
            timers: Timers): RunningCounterPresenter {
        return RunningCounterPresenter(getRunningCountersUpdatesInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiMapper,
                timers)
    }

    @Provides
    internal fun provideNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}
