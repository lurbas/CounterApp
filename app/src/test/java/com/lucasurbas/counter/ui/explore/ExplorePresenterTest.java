package com.lucasurbas.counter.ui.explore;

import static junit.framework.Assert.assertEquals;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import com.lucasurbas.counter.ui.explore.mapper.UiPostMapper;
import com.lucasurbas.counter.ui.explore.usecase.GetPostsUpdatesInteractor;
import com.lucasurbas.counter.ui.explore.usecase.LoadPostsInteractor;

@RunWith(MockitoJUnitRunner.class)
public class ExplorePresenterTest {

    private static final String USER_ID = "1";
    private static final Throwable ERROR = new Throwable("error");

    @Mock
    private GetPostsUpdatesInteractor getPostsUpdatesInteractor;
    @Mock
    private LoadPostsInteractor loadPostsInteractor;
    @Mock
    private ExplorePresenter.View view;
    @Captor
    private ArgumentCaptor<UiExploreState> argumentCaptor;

    private UiPostMapper uiPostMapper = new UiPostMapper();

    private ExplorePresenter sut;

    @Before
    public void setup() {
        given(getPostsUpdatesInteractor.getPostsUpdates()).willReturn(Observable.empty());
        sut = new ExplorePresenter(getPostsUpdatesInteractor, loadPostsInteractor, uiPostMapper);
    }

    @After
    public void tearDown() {
        sut.onCleared();
    }

    @Test
    public void firstAttachViewEmitsInitialState() {
        sut.attachView(view);

        verify(view).render(UiExploreState.initialState());
    }

    @Test
    public void loadPostsShowsLoadingIndicator() {
        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.complete());
        sut.attachView(view);

        sut.loadPosts(USER_ID);

        verify(view, times(3)).render(argumentCaptor.capture());
        List<UiExploreState> uiExploreStateList = argumentCaptor.getAllValues();
        assertEquals(UiExploreState.initialState(), uiExploreStateList.get(0));
        assertEquals(true, uiExploreStateList.get(1).getIsLoading());
        assertEquals(false, uiExploreStateList.get(2).getIsLoading());
    }

    @Test
    public void dontEmitItemsIfViewNotAttached() {
        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.complete());

        sut.loadPosts(USER_ID);

        verify(view, never()).render(argumentCaptor.capture());
    }

    @Test
    public void dontEmitItemsIfViewDetached() {
        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.complete());
        sut.attachView(view);
        sut.detachView();

        sut.loadPosts(USER_ID);

        verify(view, times(1)).render(UiExploreState.initialState());
    }

    @Test
    public void emitLastStateWhenViewReAttached() {
        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.error(ERROR));
        sut.loadPosts(USER_ID);

        sut.attachView(view);

        verify(view, times(1)).render(argumentCaptor.capture());
        UiExploreState uiExploreState = argumentCaptor.getValue();
        assertEquals(ERROR, uiExploreState.getError());
    }

}