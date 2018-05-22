package com.lucasurbas.counter.ui.explore.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UiPostItem implements UiListItem {

    public abstract int getId();

    public abstract String getTitle();

    public abstract String getImageUrl();

    public abstract boolean getIsRead();

    public static UiPostItem.Builder builder() {
        return new AutoValue_UiPostItem.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int value);

        public abstract Builder title(String value);

        public abstract Builder imageUrl(String value);

        public abstract Builder isRead(boolean value);

        public abstract UiPostItem build();
    }
}
