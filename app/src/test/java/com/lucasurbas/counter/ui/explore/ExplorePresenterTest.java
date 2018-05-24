package com.lucasurbas.counter.ui.explore;

import com.lucasurbas.counter.data.model.Counter;
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

import java.util.Arrays;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExplorePresenterTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1 = new Counter(ID_1, 0, false);
    private static final List<Counter> COUNTER_LIST = Arrays.asList(COUNTER_1);

    @Mock
    private GetCountersUpdatesInteractor getCountersUpdatesInteractor;
    @Mock
    private ExplorePresenter.View view;
    @Captor
    private ArgumentCaptor<UiExploreState> argumentCaptor;

    private UiCounterItemMapper uiCounterItemMapper = new UiCounterItemMapper();
    private PublishSubject<List<Counter>> subject;

    private ExplorePresenter sut;

    @Before
    public void setup() {
        subject = PublishSubject.create();
        given(getCountersUpdatesInteractor.getCountersUpdates()).willReturn(subject);
        sut = new ExplorePresenter(getCountersUpdatesInteractor, uiCounterItemMapper);
    }

    @After
    public void tearDown() {
        sut.onCleared();
    }

    @Test
    public void firstAttachViewEmitsInitialState() {

        sut.attachView(view);

        verify(view).render(new UiExploreState());
    }

    @Test
    public void emitNewStateWhenCounterListUpdated() {
        sut.attachView(view);

        subject.onNext(COUNTER_LIST);

        verify(view, times(2)).render(argumentCaptor.capture());
        UiExploreState uiExploreState = argumentCaptor.getValue();
        assertEquals(2, uiExploreState.getItemList().size());
        assertEquals(ID_1, uiExploreState.getItemList().get(1).getId());
    }

    @Test
    public void dontEmitItemsIfViewNotAttached() {

        subject.onNext(COUNTER_LIST);

        verify(view, never()).render(argumentCaptor.capture());
    }

    @Test
    public void dontEmitItemsIfViewDetached() {
        sut.attachView(view);
        sut.detachView();

        subject.onNext(COUNTER_LIST);

        verify(view, times(1)).render(new UiExploreState());
    }

    @Test
    public void emitLastStateWhenViewReAttached() {
        subject.onNext(COUNTER_LIST);

        sut.attachView(view);

        verify(view, times(1)).render(argumentCaptor.capture());
        UiExploreState uiExploreState = argumentCaptor.getValue();
        assertEquals(2, uiExploreState.getItemList().size());
        assertEquals(ID_1, uiExploreState.getItemList().get(1).getId());
    }

}