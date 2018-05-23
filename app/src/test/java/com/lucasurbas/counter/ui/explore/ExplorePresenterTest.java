package com.lucasurbas.counter.ui.explore;

import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper;
import com.lucasurbas.counter.ui.explore.usecase.GetCountersUpdatesInteractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExplorePresenterTest {

    private static final String USER_ID = "1";
    private static final Throwable ERROR = new Throwable("error");

    @Mock
    private GetCountersUpdatesInteractor getCountersUpdatesInteractor;
    @Mock
    private ExplorePresenter.View view;
    @Captor
    private ArgumentCaptor<UiExploreState> argumentCaptor;

    private UiCounterItemMapper uiCounterItemMapper = new UiCounterItemMapper();

    private ExplorePresenter sut;

    @Before
    public void setup() {
        given(getCountersUpdatesInteractor.getCountersUpdates()).willReturn(Observable.empty());
        sut = new ExplorePresenter(getCountersUpdatesInteractor, uiCounterItemMapper);
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

//    @Test
//    public void loadPostsShowsLoadingIndicator() {
//        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.complete());
//        sut.attachView(view);
//
//        sut.loadPosts(USER_ID);
//
//        verify(view, times(3)).render(argumentCaptor.capture());
//        List<UiExploreState> uiExploreStateList = argumentCaptor.getAllValues();
//        assertEquals(UiExploreState.initialState(), uiExploreStateList.get(0));
//        assertEquals(true, uiExploreStateList.get(1).getIsLoading());
//        assertEquals(false, uiExploreStateList.get(2).getIsLoading());
//    }
//
//    @Test
//    public void dontEmitItemsIfViewNotAttached() {
//        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.complete());
//
//        sut.loadPosts(USER_ID);
//
//        verify(view, never()).render(argumentCaptor.capture());
//    }
//
//    @Test
//    public void dontEmitItemsIfViewDetached() {
//        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.complete());
//        sut.attachView(view);
//        sut.detachView();
//
//        sut.loadPosts(USER_ID);
//
//        verify(view, times(1)).render(UiExploreState.initialState());
//    }
//
//    @Test
//    public void emitLastStateWhenViewReAttached() {
//        given(loadPostsInteractor.loadPostsToRepository(USER_ID)).willReturn(Completable.error(ERROR));
//        sut.loadPosts(USER_ID);
//
//        sut.attachView(view);
//
//        verify(view, times(1)).render(argumentCaptor.capture());
//        UiExploreState uiExploreState = argumentCaptor.getValue();
//        assertEquals(ERROR, uiExploreState.getError());
//    }

}