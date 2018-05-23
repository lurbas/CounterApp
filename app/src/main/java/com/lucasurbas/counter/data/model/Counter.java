package com.lucasurbas.counter.data.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Counter {

    public abstract int getId();

    public abstract long getValue();

    public abstract boolean getIsRunning();

    public static Builder builder() {
        return new AutoValue_Counter.Builder()
                .isRunning(false);
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int value);

        public abstract Builder value(long value);

        public abstract Builder isRunning(boolean value);

        public abstract Counter build();
    }

    public boolean isIdEqual(int id) {
        return getId() == id;
    }
}
