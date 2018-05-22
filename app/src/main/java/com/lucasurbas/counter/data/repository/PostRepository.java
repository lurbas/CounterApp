package com.lucasurbas.counter.data.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import com.lucasurbas.counter.data.model.Post;

public interface PostRepository {

    Single<Post> getPostWithId(int postId);

    Single<List<Post>> getPosts();

    Observable<List<Post>> getPostsUpdates();

    Completable updateSinglePost(Post post);

    Completable updatePosts(List<Post> postList);
}
