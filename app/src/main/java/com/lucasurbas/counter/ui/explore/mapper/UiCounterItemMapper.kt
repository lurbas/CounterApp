package com.lucasurbas.counter.ui.explore.mapper

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.ui.explore.model.UiCounterItem
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UiCounterItemMapper @Inject
constructor() {

    fun toUiCounterItem(counter: Counter): UiCounterItem {
        return UiCounterItem(
                id = counter.id,
                stringValue = formatMillis(counter.value),
                isRunning = counter.isRunning)
    }

    fun formatMillis(timeInMillis: Long): String {
        val minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1)
        val seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1)
//        val millis = timeInMillis % TimeUnit.SECONDS.toMillis(1)

        return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
    }
}
