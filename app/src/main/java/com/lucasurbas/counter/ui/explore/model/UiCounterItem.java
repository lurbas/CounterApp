package com.lucasurbas.counter.ui.explore.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UiCounterItem implements UiListItem {

    public abstract int getId();

    public abstract String getStringValue();

    public abstract boolean getIsRunning();

    public static Builder builder() {
        return new AutoValue_UiCounterItem.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int value);

        public abstract Builder stringValue(String value);

        public abstract Builder isRunning(boolean value);

        public abstract UiCounterItem build();
    }
}
