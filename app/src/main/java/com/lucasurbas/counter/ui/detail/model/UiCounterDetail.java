package com.lucasurbas.counter.ui.detail.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UiCounterDetail {

    public abstract int getId();

    public abstract String getStringValue();

    public abstract boolean getIsRunning();

    public static UiCounterDetail.Builder builder() {
        return new AutoValue_UiCounterDetail.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int value);

        public abstract Builder stringValue(String value);

        public abstract Builder isRunning(boolean value);

        public abstract UiCounterDetail build();
    }
}
