package com.lucasurbas.counter.service

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.service.usecase.GetCounterInteractor
import com.lucasurbas.counter.service.usecase.GetRunningCountersUpdatesInteractor
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.capture
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RunningCounterPresenterTest {

    @Mock
    lateinit var getRunningCountersUpdatesInteractor: GetRunningCountersUpdatesInteractor
    @Mock
    lateinit var getCounterInteractor: GetCounterInteractor
    @Mock
    lateinit var updateCounterInteractor: UpdateCounterInteractor
    @Mock
    lateinit var timers: Timers
    @Mock
    lateinit var view: RunningCounterPresenter.View
    @Captor
    lateinit var argumentCaptor: ArgumentCaptor<UiRunningCounterState>

    val uiCounterItemMapper = UiCounterItemMapper()
    lateinit var subject: PublishSubject<List<Counter>>

    lateinit var sut: RunningCounterPresenter

    @Before
    fun setup() {
        subject = PublishSubject.create()
        given(getRunningCountersUpdatesInteractor.getRunningCountersUpdates()).willReturn(subject)
        given(updateCounterInteractor.updateCounter(COUNTER_1)).willReturn(Completable.complete())
        sut = RunningCounterPresenter(getRunningCountersUpdatesInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiCounterItemMapper,
                timers)
    }

    @After
    fun tearDown() {
        sut.destroy()
    }

    @Test
    fun firstAttachViewEmitsInitialState() {

        sut.attachView(view)

        verify(view).render(UiRunningCounterState())
    }

    @Test
    fun emitNewStateWhenCounterListUpdated() {
        sut.attachView(view)

        subject.onNext(COUNTER_LIST)

        verify(view, times(2)).render(capture(argumentCaptor))
        val (runningItemList) = argumentCaptor.value
        assertEquals(1, runningItemList!!.size)
        assertEquals(ID_1, runningItemList[0].id)
    }

    @Test
    fun startCounterStartsTimerIfNotRunning() {
        given(timers.hasTimer(ID_1)).willReturn(false)
        given(getCounterInteractor.getCounter(ID_1)).willReturn(Single.just(COUNTER_1))
        sut.attachView(view)

        sut.startCounter(ID_1)

        verify<Timers>(timers, times(1)).startNew(eq(ID_1), eq(0L), any<Timers.TimerListener>())
    }

    @Test
    fun startCounterDoesntStartTimerIfAlreadyRunning() {
        given(timers.hasTimer(ID_1)).willReturn(true)
        sut.attachView(view)

        sut.startCounter(ID_1)

        verify<GetCounterInteractor>(getCounterInteractor, never()).getCounter(ID_1)
        verify<Timers>(timers, never()).startNew(eq(ID_1), eq(0L), any<Timers.TimerListener>())
    }

    @Test
    fun stopCounterStopsTimerIfRunning() {
        given(timers.hasTimer(ID_1)).willReturn(true)
        given(getCounterInteractor.getCounter(ID_1)).willReturn(Single.just(COUNTER_1))
        sut.attachView(view)

        sut.stopCounter(ID_1)

        verify<Timers>(timers, times(1)).remove(ID_1)
    }

    companion object {

        private val ID_1 = 1
        private val COUNTER_1 = Counter(ID_1, 0, false)
        private val COUNTER_LIST = Arrays.asList(COUNTER_1)
    }

}