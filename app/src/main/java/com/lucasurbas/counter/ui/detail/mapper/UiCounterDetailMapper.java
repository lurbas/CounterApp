package com.lucasurbas.counter.ui.detail.mapper;

import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.ui.detail.model.UiCounterDetail;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class UiCounterDetailMapper {

    @Inject
    public UiCounterDetailMapper() {
    }

    public UiCounterDetail toUiCounterDetail(Counter counter) {
        return UiCounterDetail.builder()
                .id(counter.getId())
                .stringValue(formatMillis(counter.getValue()))
                .isRunning(counter.getIsRunning())
                .build();
    }

    public static String formatMillis(long timeInMillis) {
        long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);
        long millis = timeInMillis % TimeUnit.SECONDS.toMillis(1);

        return String.format(Locale.ROOT, "%02d:%02d:%02d", minutes, seconds, millis);
    }
}
