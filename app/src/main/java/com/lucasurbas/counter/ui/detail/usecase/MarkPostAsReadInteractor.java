package com.lucasurbas.counter.ui.detail.usecase;

import javax.inject.Inject;

import io.reactivex.Single;
import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.data.repository.PostRepository;
import com.lucasurbas.counter.rx.InteractorSchedulers;

public class MarkPostAsReadInteractor {

    private final PostRepository postRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public MarkPostAsReadInteractor(PostRepository postRepository,
            InteractorSchedulers interactorSchedulers) {
        this.postRepository = postRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Single<Post> markPost(int postId) {
        return postRepository.getPostWithId(postId)
                .map(post -> post.toBuilder().isRead(true).build())
                .flatMap(post -> postRepository.updateSinglePost(post).toSingleDefault(post))
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
