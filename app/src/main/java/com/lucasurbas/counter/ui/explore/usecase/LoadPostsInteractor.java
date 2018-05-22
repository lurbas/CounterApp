package com.lucasurbas.counter.ui.explore.usecase;

import com.annimon.stream.Optional;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.data.repository.PostRepository;
import com.lucasurbas.counter.remote.RemotePostsService;
import com.lucasurbas.counter.remote.mapper.ApiPostMapper;
import com.lucasurbas.counter.rx.InteractorSchedulers;

public class LoadPostsInteractor {

    private final RemotePostsService remotePostsService;
    private final PostRepository postRepository;
    private final ApiPostMapper apiMapper;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public LoadPostsInteractor(RemotePostsService remotePostsService,
            PostRepository postRepository,
            ApiPostMapper apiMapper,
            InteractorSchedulers interactorSchedulers) {
        this.remotePostsService = remotePostsService;
        this.postRepository = postRepository;
        this.apiMapper = apiMapper;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Completable loadPostsToRepository(String userId) {
        return Single.zip(
                postRepository.getPosts(),
                remotePostsService.getPosts(userId)
                        .flatMap(posts -> Observable.fromIterable(posts.getPostList())
                                .map(apiMapper::toPost)
                                .toList()),
                this::createUpdatedList)
                .flatMapCompletable(postRepository::updatePosts)
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }

    private List<Post> createUpdatedList(List<Post> oldPostList, List<Post> newPostList) {
        List<Post> updatedList = new ArrayList<>();
        for (Post newPost : newPostList) {
            Optional<Post> oldPost = getPostWithId(oldPostList, newPost.getId());
            if (oldPost.isPresent()) {
                updatedList.add(newPost.toBuilder().isRead(oldPost.get().getIsRead()).build());
            } else {
                updatedList.add(newPost);
            }
        }
        return updatedList;
    }

    private Optional<Post> getPostWithId(List<Post> postList, int id) {
        for (Post post : postList) {
            if (post.isIdEqual(id)) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }
}
