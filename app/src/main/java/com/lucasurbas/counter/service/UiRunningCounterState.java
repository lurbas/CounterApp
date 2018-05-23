package com.lucasurbas.counter.service;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;

import java.util.List;

@AutoValue
public abstract class UiRunningCounterState {

    @Nullable
    abstract List<UiCounterItem> getRunningItemList();
    @Nullable
    abstract Throwable getError();

    abstract boolean getIsStarted();

    private static Builder builder() {
        return new AutoValue_UiRunningCounterState.Builder().isStarted(false);
    }

    abstract Builder toBuilder();

    static UiRunningCounterState initialState() {
        return builder().build();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder runningItemList(List<UiCounterItem> value);

        abstract Builder error(Throwable value);

        abstract Builder isStarted(boolean value);

        abstract UiRunningCounterState build();
    }

    public interface Part {

        UiRunningCounterState computeNewState(UiRunningCounterState previousState);

        class Error implements UiRunningCounterState.Part {

            private Throwable error;

            public Error(Throwable error) {
                this.error = error;
            }

            @Override
            public UiRunningCounterState computeNewState(UiRunningCounterState previousState) {
                return previousState.toBuilder()
                        .error(error)
                        .build();
            }
        }

        class RunningCounterList implements UiRunningCounterState.Part {

            private List<UiCounterItem> uiCounterItemList;

            public RunningCounterList(List<UiCounterItem> uiCounterItemList) {
                this.uiCounterItemList = uiCounterItemList;
            }

            @Override
            public UiRunningCounterState computeNewState(UiRunningCounterState previousState) {
                return previousState.toBuilder()
                        .runningItemList(uiCounterItemList)
                        .error(null)
                        .build();
            }
        }

        class CounterUpdated implements UiRunningCounterState.Part {

            @Override
            public UiRunningCounterState computeNewState(UiRunningCounterState previousState) {
                return previousState.toBuilder()
                        .isStarted(true)
                        .build();
            }
        }
    }
}
