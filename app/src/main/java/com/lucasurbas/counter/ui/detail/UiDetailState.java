package com.lucasurbas.counter.ui.detail;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import com.lucasurbas.counter.data.model.Post;

@AutoValue
public abstract class UiDetailState {

    abstract boolean getIsLoading();
    @Nullable
    abstract String getUrl();
    @Nullable
    abstract Throwable getError();

    private static UiDetailState.Builder builder() {
        return new AutoValue_UiDetailState.Builder()
                .isLoading(false);
    }

    abstract Builder toBuilder();

    static UiDetailState initialState() {
        return builder().build();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder isLoading(boolean value);

        abstract Builder url(String value);

        abstract Builder error(Throwable value);

        abstract UiDetailState build();
    }

    public interface Part {

        UiDetailState computeNewState(UiDetailState previousState);

        class Loading implements UiDetailState.Part {

            @Override
            public UiDetailState computeNewState(UiDetailState previousState) {
                return previousState.toBuilder()
                        .isLoading(true)
                        .error(null)
                        .build();
            }
        }

        class Error implements UiDetailState.Part {

            private Throwable error;

            public Error(Throwable error) {
                this.error = error;
            }

            @Override
            public UiDetailState computeNewState(UiDetailState previousState) {
                return previousState.toBuilder()
                        .isLoading(false)
                        .error(error)
                        .build();
            }
        }

        class LoadedPost implements UiDetailState.Part {

            private Post post;

            public LoadedPost(Post post) {
                this.post = post;
            }

            @Override
            public UiDetailState computeNewState(UiDetailState previousState) {
                return previousState.toBuilder()
                        .isLoading(false)
                        .url(post.getLinkUrl())
                        .error(null)
                        .build();
            }
        }
    }
}
