package com.lucasurbas.counter.ui.detail;

import android.arch.lifecycle.ViewModel;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.ui.detail.usecase.GetPostInteractor;
import com.lucasurbas.counter.ui.detail.usecase.MarkPostAsReadInteractor;

public class DetailPresenter extends ViewModel {

    private final GetPostInteractor getPostInteractor;
    private final MarkPostAsReadInteractor markPostAsReadInteractor;

    private final PublishSubject<Integer> getPostActionSubject = PublishSubject.create();
    private final PublishSubject<Integer> markPostAsReadActionSubject = PublishSubject.create();
    private final BehaviorSubject<UiDetailState> stateSubject = BehaviorSubject.create();

    private final CompositeDisposable stateDisposables = new CompositeDisposable();
    private Disposable mainDisposable;

    public DetailPresenter(GetPostInteractor getPostInteractor,
            MarkPostAsReadInteractor markPostAsReadInteractor) {
        this.getPostInteractor = getPostInteractor;
        this.markPostAsReadInteractor = markPostAsReadInteractor;

        init();
    }

    private void init() {
        Observable<UiDetailState.Part> getPostAction = getPostActionSubject
                .flatMap(postId -> getPostInteractor.getPost(postId).toObservable()
                        .map((Function<Post, UiDetailState.Part>) UiDetailState.Part.LoadedPost::new)
                        .startWith(new UiDetailState.Part.Loading())
                        .onErrorReturn(UiDetailState.Part.Error::new));

        Observable<UiDetailState.Part> markPostAsReadAction = markPostAsReadActionSubject
                .flatMap(postId -> markPostAsReadInteractor.markPost(postId).toObservable()
                        .map((Function<Post, UiDetailState.Part>) UiDetailState.Part.LoadedPost::new)
                        .onErrorReturn(UiDetailState.Part.Error::new));

        mainDisposable = Observable.merge(getPostAction, markPostAsReadAction)
                .scan(UiDetailState.initialState(), this::viewStateReducer)
                .distinctUntilChanged()
                .subscribe(stateSubject::onNext);
    }

    private UiDetailState viewStateReducer(UiDetailState previousState, UiDetailState.Part changes) {
        return changes.computeNewState(previousState);
    }

    public void attachView(DetailPresenter.View view) {
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

    public void getPostIfNeeded(final int postId) {
        if (!stateSubject.hasValue() || stateSubject.getValue().equals(UiDetailState.initialState())) {
            getPostActionSubject.onNext(postId);
        }
    }

    public void markPostAsRead(int postId) {
        markPostAsReadActionSubject.onNext(postId);
    }

    public interface View {

        void render(UiDetailState exploreState);

    }
}
