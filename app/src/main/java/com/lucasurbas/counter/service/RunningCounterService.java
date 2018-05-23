package com.lucasurbas.counter.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.di.helper.InjectHelper;
import com.lucasurbas.counter.service.di.RunningCounterServiceModule;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;
import com.lucasurbas.counter.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RunningCounterService extends Service implements RunningCounterPresenter.View {

    private static final String COUNTER_ID_KEY = "counter_id_key";

    private static final int NOTIFICATION_ID = 11;
    private static final String CHANNEL_ID = "main";

    public static final String ACTION_START = "com.lucasurbas.counterservice.action.start";
    public static final String ACTION_STOP = "com.lucasurbas.counterservice.action.stop";

    @Inject
    RunningCounterPresenter presenter;
    @Inject
    NotificationManager notificationManager;

    public static Intent newInstance(Context context, String action, int counterId) {
        Intent intent = new Intent(context, RunningCounterService.class);
        intent.setAction(action);
        intent.putExtra(COUNTER_ID_KEY, counterId);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InjectHelper.getAppComponent(this)
                .plus(new RunningCounterServiceModule())
                .inject(this);

        startForeground(NOTIFICATION_ID, getNotification(new ArrayList<>()));

        presenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals(ACTION_START)) {
            int counterId = intent.getIntExtra(COUNTER_ID_KEY, 0);
            presenter.startCounter(counterId);
        } else if (intent.getAction() != null && intent.getAction().equals(ACTION_STOP)) {
            int counterId = intent.getIntExtra(COUNTER_ID_KEY, 0);
            presenter.stopCounter(counterId);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        presenter.detachView();
        presenter.destroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void render(UiRunningCounterState runningCounterState) {
        if (runningCounterState.getRunningItemList() != null) {
            notificationManager.notify(NOTIFICATION_ID, getNotification(runningCounterState.getRunningItemList()));
            if (runningCounterState.getRunningItemList().isEmpty() && runningCounterState.getIsStarted()) {
                stopForeground(true);
                stopSelf();
            }
        }
    }

    private Notification getNotification(List<UiCounterItem> runningCounters) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        if (runningCounters.isEmpty()) {
            return builder.build();
        } else if (runningCounters.size() == 1) {
            builder.setContentText(runningCounters.get(0).getStringValue());
            return builder.build();
        } else {
            builder.setContentText(getString(R.string.notification_running_format, runningCounters.size()));
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (UiCounterItem value : runningCounters) {
                inboxStyle.addLine(value.getStringValue());
            }
            builder.setStyle(inboxStyle);
            return builder.build();
        }
    }
}
