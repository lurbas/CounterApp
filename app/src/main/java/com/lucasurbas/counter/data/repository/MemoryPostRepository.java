package com.lucasurbas.counter.data.repository;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import com.lucasurbas.counter.data.model.Post;

public class MemoryPostRepository implements PostRepository {

    private final List<Post> postList;
    private BehaviorSubject<List<Post>> postListSubject;

    public MemoryPostRepository() {
        postList = new ArrayList<>();
        postListSubject = BehaviorSubject.create();
    }

    @Override
    public Single<Post> getPostWithId(final int postId) {
        return Observable.fromIterable(postList)
                .filter(post -> post.isIdEqual(postId))
                .firstOrError();
    }

    @Override
    public Single<List<Post>> getPosts() {
        return Single.fromCallable(() -> postList);
    }

    @Override
    public Observable<List<Post>> getPostsUpdates() {
        return postListSubject;
    }

    @Override
    public Completable updateSinglePost(Post updatedPost) {
        return Completable.fromRunnable(() -> {
            List<Post> list = Stream.of(postList)
                    .map(post -> post.isIdEqual(updatedPost.getId()) ? updatedPost : post)
                    .toList();
            postList.clear();
            postList.addAll(list);
            postListSubject.onNext(postList);
        });
    }

    @Override
    public Completable updatePosts(List<Post> newPostList) {
        return Completable.fromRunnable(() -> {
            postList.clear();
            postList.addAll(newPostList);
            postListSubject.onNext(postList);
        });
    }
}
