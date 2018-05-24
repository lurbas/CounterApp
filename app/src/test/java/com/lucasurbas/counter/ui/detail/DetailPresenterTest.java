package com.lucasurbas.counter.ui.detail;

import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.service.usecase.GetCounterInteractor;
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor;
import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper;
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DetailPresenterTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1 = new Counter(ID_1, 0, false);
    private static final Counter COUNTER_1_MILLIS = new Counter(ID_1, 100, false);

    @Mock
    private GetCounterUpdateInteractor getCounterUpdateInteractor;
    @Mock
    private GetCounterInteractor getCounterInteractor;
    @Mock
    private UpdateCounterInteractor updateCounterInteractor;
    @Mock
    private DetailPresenter.View view;
    @Captor
    private ArgumentCaptor<UiDetailState> argumentCaptor;

    private UiCounterDetailMapper uiCounterDetailMapper = new UiCounterDetailMapper();
    private PublishSubject<Counter> subject;

    private DetailPresenter sut;

    @Before
    public void setup() {
        subject = PublishSubject.create();
        given(getCounterUpdateInteractor.getCounterUpdates(ID_1)).willReturn(subject);
        sut = new DetailPresenter(getCounterUpdateInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiCounterDetailMapper);
    }

    @After
    public void tearDown() {
        sut.onCleared();
    }

    @Test
    public void firstAttachViewEmitsInitialState() {

        sut.attachView(view);

        verify(view).render(new UiDetailState());
    }

    @Test
    public void dontEmitItemsIfViewNotAttached() {
        subject.onNext(COUNTER_1);

        verify(view, never()).render(argumentCaptor.capture());
    }

    @Test
    public void getCounterDoesntEmitItemsIfViewNotAttached2() {
        subject.onNext(COUNTER_1);

        sut.getCounter(ID_1);

        verify(view, never()).render(argumentCaptor.capture());
    }

    @Test
    public void emitNewStateWhenCounterUpdated() {
        sut.attachView(view);
        sut.getCounter(ID_1);

        subject.onNext(COUNTER_1);

        verify(view, times(2)).render(argumentCaptor.capture());
        UiDetailState uiDetailState = argumentCaptor.getValue();
        assertEquals(ID_1, uiDetailState.getCounter().getId());
    }

    @Test
    public void updateInitialValueTriggersCounterUpdate() {
        given(getCounterInteractor.getCounter(ID_1)).willReturn(Single.just(COUNTER_1));
        when(updateCounterInteractor.updateCounter(COUNTER_1_MILLIS)).thenAnswer(new Answer<Completable>() {
            @Override
            public Completable answer(InvocationOnMock invocation) throws Throwable {
                subject.onNext(COUNTER_1_MILLIS);
                return Completable.complete();
            }
        });
        sut.attachView(view);
        sut.getCounter(ID_1);
        subject.onNext(COUNTER_1);

        sut.updateInitialValue(ID_1, 100);

        verify(view, times(3)).render(argumentCaptor.capture());
        UiDetailState uiDetailState = argumentCaptor.getValue();
        assertEquals("00:00:10", uiDetailState.getCounter().getStringValue());
    }

}