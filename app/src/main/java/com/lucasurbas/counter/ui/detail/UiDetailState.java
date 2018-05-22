package com.lucasurbas.counter.ui.detail;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.lucasurbas.counter.ui.detail.model.UiCounterDetail;

@AutoValue
public abstract class UiDetailState {

    @Nullable
    abstract UiCounterDetail getCounter();
    @Nullable
    abstract Throwable getError();

    private static UiDetailState.Builder builder() {
        return new AutoValue_UiDetailState.Builder();
    }

    abstract Builder toBuilder();

    static UiDetailState initialState() {
        return builder().build();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder counter(UiCounterDetail value);

        abstract Builder error(Throwable value);

        abstract UiDetailState build();
    }

    public interface Part {

        UiDetailState computeNewState(UiDetailState previousState);

        class Error implements UiDetailState.Part {

            private Throwable error;

            public Error(Throwable error) {
                this.error = error;
            }

            @Override
            public UiDetailState computeNewState(UiDetailState previousState) {
                return previousState.toBuilder()
                        .error(error)
                        .build();
            }
        }

        class CounterItem implements UiDetailState.Part {

            private UiCounterDetail counter;

            public CounterItem(UiCounterDetail counter) {
                this.counter = counter;
            }

            @Override
            public UiDetailState computeNewState(UiDetailState previousState) {
                return previousState.toBuilder()
                        .counter(counter)
                        .error(null)
                        .build();
            }
        }
    }
}
