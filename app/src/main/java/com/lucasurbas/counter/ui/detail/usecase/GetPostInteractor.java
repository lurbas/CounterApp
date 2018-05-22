package com.lucasurbas.counter.ui.detail.usecase;

import javax.inject.Inject;

import io.reactivex.Single;
import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.data.repository.PostRepository;
import com.lucasurbas.counter.rx.InteractorSchedulers;

public class GetPostInteractor {

    private final PostRepository postRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public GetPostInteractor(PostRepository postRepository,
            InteractorSchedulers interactorSchedulers) {
        this.postRepository = postRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Single<Post> getPost(int postId) {
        return postRepository.getPostWithId(postId)
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
