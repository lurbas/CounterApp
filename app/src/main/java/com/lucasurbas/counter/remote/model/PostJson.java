package com.lucasurbas.counter.remote.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class PostJson {

    @SerializedName("id")
    public abstract int getId();

    @SerializedName("large_thumbnail_url")
    @Nullable
    public abstract String getThumbnailUrl();

    @SerializedName("link_url")
    @Nullable
    public abstract String getLinkUrl();

    public static TypeAdapter<PostJson> typeAdapter(Gson gson) {
        return new AutoValue_PostJson.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_PostJson.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(int value);

        public abstract Builder thumbnailUrl(String value);

        public abstract Builder linkUrl(String value);

        public abstract PostJson build();
    }
}
