package com.lucasurbas.counter.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.lucasurbas.counter.R
import com.lucasurbas.counter.app.di.helper.InjectHelper
import com.lucasurbas.counter.service.di.RunningCounterServiceModule
import com.lucasurbas.counter.ui.explore.model.UiCounterItem
import com.lucasurbas.counter.ui.main.MainActivity
import java.util.*
import javax.inject.Inject

class RunningCounterService : Service(), RunningCounterPresenter.View {

    companion object {

        private val COUNTER_ID_KEY = "counter_id_key"

        private val NOTIFICATION_ID = 11
        private val CHANNEL_ID = "main"

        val ACTION_START = "com.lucasurbas.counterservice.action.start"
        val ACTION_STOP = "com.lucasurbas.counterservice.action.stop"

        fun newInstance(context: Context, action: String, counterId: Int): Intent {
            val intent = Intent(context, RunningCounterService::class.java)
            intent.action = action
            intent.putExtra(COUNTER_ID_KEY, counterId)
            return intent
        }
    }

    @Inject
    lateinit var presenter: RunningCounterPresenter
    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        InjectHelper.getAppComponent(this)
                .plus(RunningCounterServiceModule())
                .inject(this)

        startForeground(NOTIFICATION_ID, getNotification(ArrayList()))

        presenter.attachView(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.action?.let {
            when (it) {
                ACTION_START -> presenter.startCounter(intent.getIntExtra(COUNTER_ID_KEY, 0))
                ACTION_STOP -> presenter.stopCounter(intent.getIntExtra(COUNTER_ID_KEY, 0))
            }
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        presenter.detachView()
        presenter.destroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun render(runningCounterState: UiRunningCounterState) {
        runningCounterState.runningItemList?.let {
            notificationManager.notify(NOTIFICATION_ID, getNotification(it))
            if (it.isEmpty() && runningCounterState.isStarted) {
                stopForeground(true)
                stopSelf()
            }
        }
    }

    private fun getNotification(runningCounters: List<UiCounterItem>): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true)

        if (runningCounters.isEmpty()) {
            return builder.build()
        } else if (runningCounters.size == 1) {
            builder.setContentText(runningCounters[0].stringValue)
            return builder.build()
        } else {
            builder.setContentText(getString(R.string.notification_running_format, runningCounters.size))
            val inboxStyle = NotificationCompat.InboxStyle()
            for (counter in runningCounters) {
                inboxStyle.addLine(counter.stringValue)
            }
            builder.setStyle(inboxStyle)
            return builder.build()
        }
    }
}
