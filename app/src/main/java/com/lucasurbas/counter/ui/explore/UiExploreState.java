package com.lucasurbas.counter.ui.explore;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

import com.lucasurbas.counter.ui.explore.model.UiHeaderItem;
import com.lucasurbas.counter.ui.explore.model.UiListItem;
import com.lucasurbas.counter.ui.explore.model.UiPostItem;

@AutoValue
public abstract class UiExploreState {

    abstract boolean getIsLoading();

    abstract List<UiListItem> getItemList();

    @Nullable
    abstract Throwable getError();

    private static UiExploreState.Builder builder() {
        return new AutoValue_UiExploreState.Builder()
                .isLoading(false)
                .itemList(new ArrayList<>());
    }

    abstract Builder toBuilder();

    static UiExploreState initialState() {
        return builder().build();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder isLoading(boolean value);

        abstract Builder itemList(List<UiListItem> value);

        abstract Builder error(Throwable value);

        abstract UiExploreState build();
    }

    public interface Part {

        UiExploreState computeNewState(UiExploreState previousState);

        class Loading implements UiExploreState.Part {

            @Override
            public UiExploreState computeNewState(UiExploreState previousState) {
                return previousState.toBuilder()
                        .isLoading(true)
                        .error(null)
                        .build();
            }
        }

        class Error implements UiExploreState.Part {

            private Throwable error;

            public Error(Throwable error) {
                this.error = error;
            }

            @Override
            public UiExploreState computeNewState(UiExploreState previousState) {
                return previousState.toBuilder()
                        .isLoading(false)
                        .error(error)
                        .build();
            }
        }

        class PostsLoaded implements UiExploreState.Part {

            @Override
            public UiExploreState computeNewState(UiExploreState previousState) {
                return previousState.toBuilder()
                        .isLoading(false)
                        .build();
            }
        }

        class Posts implements UiExploreState.Part {

            private List<UiPostItem> uiPostItemList;

            public Posts(List<UiPostItem> uiPostItemList) {
                this.uiPostItemList = uiPostItemList;
            }

            @Override
            public UiExploreState computeNewState(UiExploreState previousState) {
                return previousState.toBuilder()
                        .itemList(calculateListData(uiPostItemList))
                        .build();
            }

            private List<UiListItem> calculateListData(List<UiPostItem> uiPostItemList) {
                List<UiListItem> data = new ArrayList<>();
                data.add(UiHeaderItem.newInstance());
                data.addAll(uiPostItemList);
                return data;
            }
        }
    }
}
