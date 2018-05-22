package com.lucasurbas.counter.ui.explore;

import android.arch.lifecycle.ViewModel;

import com.annimon.stream.Optional;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import com.lucasurbas.counter.ui.explore.mapper.UiPostMapper;
import com.lucasurbas.counter.ui.explore.usecase.GetPostsUpdatesInteractor;
import com.lucasurbas.counter.ui.explore.usecase.LoadPostsInteractor;

public class ExplorePresenter extends ViewModel {

    private final GetPostsUpdatesInteractor getPostsUpdatesInteractor;
    private final LoadPostsInteractor loadPostsInteractor;
    private final UiPostMapper uiPostMapper;

    private final PublishSubject<String> loadPostsActionSubject = PublishSubject.create();
    private final BehaviorSubject<UiExploreState> stateSubject = BehaviorSubject.create();

    private final CompositeDisposable stateDisposables = new CompositeDisposable();
    private Disposable mainDisposable;

    public ExplorePresenter(GetPostsUpdatesInteractor getPostsUpdatesInteractor,
            LoadPostsInteractor loadPostsInteractor,
            UiPostMapper uiPostMapper) {
        this.getPostsUpdatesInteractor = getPostsUpdatesInteractor;
        this.loadPostsInteractor = loadPostsInteractor;
        this.uiPostMapper = uiPostMapper;

        init();
    }

    private void init() {
        Observable<UiExploreState.Part> loadPostsAction = loadPostsActionSubject
                .flatMap(userId -> loadPostsInteractor.loadPostsToRepository(userId)
                        .andThen(Observable.fromCallable((Callable<UiExploreState.Part>) UiExploreState.Part.PostsLoaded::new))
                        .startWith(new UiExploreState.Part.Loading())
                        .onErrorReturn(UiExploreState.Part.Error::new));

        Observable<UiExploreState.Part> getPostsUpdates = getPostsUpdatesInteractor.getPostsUpdates()
                        .flatMap(postList -> Observable.fromIterable(postList)
                                .map(uiPostMapper::toUiPostItem)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .toList().toObservable())
                        .map(UiExploreState.Part.Posts::new);

        mainDisposable = Observable.merge(loadPostsAction, getPostsUpdates)
                .scan(UiExploreState.initialState(), this::viewStateReducer)
                .distinctUntilChanged()
                .subscribe(stateSubject::onNext);
    }

    private UiExploreState viewStateReducer(UiExploreState previousState, UiExploreState.Part changes) {
        return changes.computeNewState(previousState);
    }

    public void attachView(ExplorePresenter.View view) {
        Disposable disposable = stateSubject.subscribe(view::render);
        stateDisposables.add(disposable);
    }

    public void detachView() {
        stateDisposables.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mainDisposable.dispose();
    }

    public void loadPosts(final String userId) {
        loadPostsActionSubject.onNext(userId);
    }

    public void loadPostsIfNeeded(final String userId) {
        if (!stateSubject.hasValue() || stateSubject.getValue().equals(UiExploreState.initialState())) {
            loadPostsActionSubject.onNext(userId);
        }
    }

    public interface View {

        void render(UiExploreState exploreState);

    }
}
