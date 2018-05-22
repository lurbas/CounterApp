package com.lucasurbas.counter.ui.explore.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.data.repository.PostRepository;
import com.lucasurbas.counter.rx.InteractorSchedulers;

public class GetPostsUpdatesInteractor {

    private final PostRepository postRepository;
    private final InteractorSchedulers interactorSchedulers;

    @Inject
    public GetPostsUpdatesInteractor(PostRepository postRepository,
            InteractorSchedulers interactorSchedulers){
        this.postRepository = postRepository;
        this.interactorSchedulers = interactorSchedulers;
    }

    public Observable<List<Post>> getPostsUpdates(){
        return postRepository.getPostsUpdates()
                .subscribeOn(interactorSchedulers.getBackgroundScheduler())
                .observeOn(interactorSchedulers.getMainThreadScheduler());
    }
}
