package com.lucasurbas.counter.ui.detail.mapper

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.ui.detail.model.UiCounterDetail
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UiCounterDetailMapper @Inject constructor() {

    fun toUiCounterDetail(counter: Counter): UiCounterDetail {
        return UiCounterDetail(id = counter.id,
                stringValue = formatMillis(counter.value),
                isRunning = counter.isRunning,
                color = counter.color)
    }

    fun formatMillis(timeInMillis: Long): String {
        val minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1)
        val seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1)
        val millis = timeInMillis % TimeUnit.SECONDS.toMillis(1) / 10

        return String.format(Locale.ROOT, "%02d:%02d:%02d", minutes, seconds, millis)
    }
}
