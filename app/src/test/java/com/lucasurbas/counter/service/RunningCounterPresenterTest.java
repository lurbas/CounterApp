package com.lucasurbas.counter.service;

import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.service.usecase.GetCounterInteractor;
import com.lucasurbas.counter.service.usecase.GetRunningCountersUpdatesInteractor;
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor;
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper;

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

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RunningCounterPresenterTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1 = Counter.builder().id(ID_1).value(0).build();
    private static final List<Counter> COUNTER_LIST = Arrays.asList(COUNTER_1);

    @Mock
    GetRunningCountersUpdatesInteractor getRunningCountersUpdatesInteractor;
    @Mock
    GetCounterInteractor getCounterInteractor;
    @Mock
    UpdateCounterInteractor updateCounterInteractor;
    @Mock
    Timers timers;
    @Mock
    private RunningCounterPresenter.View view;
    @Captor
    private ArgumentCaptor<UiRunningCounterState> argumentCaptor;

    private UiCounterItemMapper uiCounterItemMapper = new UiCounterItemMapper();
    private PublishSubject<List<Counter>> subject;

    private RunningCounterPresenter sut;

    @Before
    public void setup() {
        subject = PublishSubject.create();
        given(getRunningCountersUpdatesInteractor.getRunningCountersUpdates()).willReturn(subject);
        given(updateCounterInteractor.updateCounter(COUNTER_1)).willReturn(Completable.complete());
        sut = new RunningCounterPresenter(getRunningCountersUpdatesInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiCounterItemMapper,
                timers);
    }

    @After
    public void tearDown() {
        sut.destroy();
    }

    @Test
    public void firstAttachViewEmitsInitialState() {

        sut.attachView(view);

        verify(view).render(UiRunningCounterState.initialState());
    }

    @Test
    public void emitNewStateWhenCounterListUpdated() {
        sut.attachView(view);

        subject.onNext(COUNTER_LIST);

        verify(view, times(2)).render(argumentCaptor.capture());
        UiRunningCounterState uiRunningCounterState = argumentCaptor.getValue();
        assertEquals(1, uiRunningCounterState.getRunningItemList().size());
        assertEquals(ID_1, uiRunningCounterState.getRunningItemList().get(0).getId());
    }

    @Test
    public void startCounterStartsTimerIfNotRunning() {
        given(timers.hasTimer(ID_1)).willReturn(false);
        given(getCounterInteractor.getCounter(ID_1)).willReturn(Single.just(COUNTER_1));
        sut.attachView(view);

        sut.startCounter(ID_1);

        verify(timers, times(1)).startNew(eq(ID_1), eq(0L), any());
    }

    @Test
    public void startCounterDoesntStartTimerIfAlreadyRunning() {
        given(timers.hasTimer(ID_1)).willReturn(true);
        sut.attachView(view);

        sut.startCounter(ID_1);

        verify(getCounterInteractor, never()).getCounter(ID_1);
        verify(timers, never()).startNew(eq(ID_1), eq(0L), any());
    }

    @Test
    public void stopCounterStopsTimerIfRunning() {
        given(timers.hasTimer(ID_1)).willReturn(true);
        given(getCounterInteractor.getCounter(ID_1)).willReturn(Single.just(COUNTER_1));
        sut.attachView(view);

        sut.stopCounter(ID_1);

        verify(timers, times(1)).remove(ID_1);
    }

}