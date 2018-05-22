package com.lucasurbas.counter.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Post {

    public abstract int getId();
    @Nullable
    public abstract String getLinkUrl();
    @Nullable
    public abstract String getImageUrl();

    public abstract boolean getIsRead();

    public static Builder builder() {
        return new AutoValue_Post.Builder()
                .isRead(false);
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int value);

        public abstract Builder linkUrl(String value);

        public abstract Builder imageUrl(String value);

        public abstract Builder isRead(boolean value);

        public abstract Post build();
    }

    public boolean isIdEqual(int id){
        return getId() == id;
    }
}
