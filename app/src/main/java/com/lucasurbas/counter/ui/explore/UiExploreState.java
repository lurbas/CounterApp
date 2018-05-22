package com.lucasurbas.counter.ui.explore;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;
import com.lucasurbas.counter.ui.explore.model.UiHeaderItem;
import com.lucasurbas.counter.ui.explore.model.UiListItem;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class UiExploreState {

    abstract List<UiListItem> getItemList();
    @Nullable
    abstract Throwable getError();

    private static UiExploreState.Builder builder() {
        return new AutoValue_UiExploreState.Builder()
                .itemList(new ArrayList<>());
    }

    abstract Builder toBuilder();

    static UiExploreState initialState() {
        return builder().build();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder itemList(List<UiListItem> value);

        abstract Builder error(Throwable value);

        abstract UiExploreState build();
    }

    public interface Part {

        UiExploreState computeNewState(UiExploreState previousState);

        class Error implements UiExploreState.Part {

            private Throwable error;

            public Error(Throwable error) {
                this.error = error;
            }

            @Override
            public UiExploreState computeNewState(UiExploreState previousState) {
                return previousState.toBuilder()
                        .error(error)
                        .build();
            }
        }

        class CounterList implements UiExploreState.Part {

            private List<UiCounterItem> uiCounterItemList;

            public CounterList(List<UiCounterItem> uiCounterItemList) {
                this.uiCounterItemList = uiCounterItemList;
            }

            @Override
            public UiExploreState computeNewState(UiExploreState previousState) {
                return previousState.toBuilder()
                        .error(null)
                        .itemList(calculateListData(uiCounterItemList))
                        .build();
            }

            private List<UiListItem> calculateListData(List<UiCounterItem> uiCounterItemList) {
                List<UiListItem> data = new ArrayList<>();
                data.add(UiHeaderItem.newInstance());
                data.addAll(uiCounterItemList);
                return data;
            }
        }
    }
}
