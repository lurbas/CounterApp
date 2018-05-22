package com.lucasurbas.counter.ui.explore.mapper;

import android.util.Log;

import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class UiCounterItemMapper {

    @Inject
    public UiCounterItemMapper() {
    }

    public UiCounterItem toUiCounterItem(Counter counter) {
        Log.v("TAG_UiCounterItemMapper", counter.toString());
        return UiCounterItem.builder()
                .id(counter.getId())
                .stringValue(formatMillis(counter.getValue()))
                .isRunning(counter.getIsRunning())
                .build();
    }

    public static String formatMillis(long timeInMillis) {
        long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);
//        long millis = timeInMillis % TimeUnit.SECONDS.toMillis(1);

        return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds);
    }
}
