package com.lucasurbas.counter.ui.detail.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CounterUpdateData {

    public abstract int getId();

    public abstract long getNewValue();

    public static CounterUpdateData newInstance(int counterId, long millis){
        return new AutoValue_CounterUpdateData(counterId, millis);
    }
}
