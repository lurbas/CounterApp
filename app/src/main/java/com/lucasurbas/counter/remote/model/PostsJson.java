package com.lucasurbas.counter.remote.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class PostsJson {

    @SerializedName("posts")
    public abstract List<PostJson> getPostList();

    public static TypeAdapter<PostsJson> typeAdapter(Gson gson) {
        return new AutoValue_PostsJson.GsonTypeAdapter(gson);
    }
}
