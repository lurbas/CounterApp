package com.lucasurbas.counter.remote;

import com.lucasurbas.counter.remote.model.PostsJson;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemotePostsService {

    @GET("pub/posts.json")
    Single<PostsJson> getPosts(@Query("profile_id") String userId);
}
