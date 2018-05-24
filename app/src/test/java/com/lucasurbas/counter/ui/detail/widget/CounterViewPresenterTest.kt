package com.lucasurbas.counter.ui.detail.widget

import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class CounterViewPresenterTest {

    @Mock
    lateinit var view: CounterViewPresenter.View
    @Captor
    lateinit var argumentCaptor: ArgumentCaptor<UiCounterViewState>

    lateinit var sut: CounterViewPresenter

    @Before
    fun setup() {
        sut = CounterViewPresenter()
    }

    @After
    fun tearDown() {
        sut.destroy()
    }

    @Test
    fun dontEmitItemsIfViewNotAttached() {
        sut.newValue(100);

        verify(view, never()).render(capture(argumentCaptor))
    }

    @Test
    fun emitNewStateWhenCounterUpdated() {
        sut.start(view)

        sut.newValue(100L);

        Mockito.verify(view, Mockito.times(2)).render(capture(argumentCaptor))
        val counterState = argumentCaptor.value as UiCounterViewState
        Assert.assertEquals(100L, counterState.value)
    }

    @Test
    fun animateSecondsRightDigitChange() {
        sut.start(view)

        sut.newValue(TimeUnit.SECONDS.toMillis(1));
        sut.newValue(TimeUnit.SECONDS.toMillis(2));

        Mockito.verify(view, Mockito.times(3)).render(capture(argumentCaptor))
        val counterState = argumentCaptor.value as UiCounterViewState
        Assert.assertEquals(false, counterState.minuteLeftAnimate)
        Assert.assertEquals(false, counterState.minuteRightAnimate)
        Assert.assertEquals(false, counterState.secondLeftAnimate)
        Assert.assertEquals(true, counterState.secondRightAnimate)
    }

    @Test
    fun animateSecondsLeftDigitChange() {
        sut.start(view)

        sut.newValue(TimeUnit.SECONDS.toMillis(10));
        sut.newValue(TimeUnit.SECONDS.toMillis(20));

        Mockito.verify(view, Mockito.times(3)).render(capture(argumentCaptor))
        val counterState = argumentCaptor.value as UiCounterViewState
        Assert.assertEquals(false, counterState.minuteLeftAnimate)
        Assert.assertEquals(false, counterState.minuteRightAnimate)
        Assert.assertEquals(true, counterState.secondLeftAnimate)
        Assert.assertEquals(false, counterState.secondRightAnimate)
    }

    @Test
    fun animateMinutesRightDigitChange() {
        sut.start(view)

        sut.newValue(TimeUnit.MINUTES.toMillis(1));
        sut.newValue(TimeUnit.MINUTES.toMillis(2));

        Mockito.verify(view, Mockito.times(3)).render(capture(argumentCaptor))
        val counterState = argumentCaptor.value as UiCounterViewState
        Assert.assertEquals(false, counterState.minuteLeftAnimate)
        Assert.assertEquals(true, counterState.minuteRightAnimate)
        Assert.assertEquals(false, counterState.secondLeftAnimate)
        Assert.assertEquals(false, counterState.secondRightAnimate)
    }

    @Test
    fun animateMinutesLeftDigitChange() {
        sut.start(view)

        sut.newValue(TimeUnit.MINUTES.toMillis(10));
        sut.newValue(TimeUnit.MINUTES.toMillis(20));

        Mockito.verify(view, Mockito.times(3)).render(capture(argumentCaptor))
        val counterState = argumentCaptor.value as UiCounterViewState
        Assert.assertEquals(true, counterState.minuteLeftAnimate)
        Assert.assertEquals(false, counterState.minuteRightAnimate)
        Assert.assertEquals(false, counterState.secondLeftAnimate)
        Assert.assertEquals(false, counterState.secondRightAnimate)
    }

    @Test
    fun animateAllDigitsChange() {
        sut.start(view)

        sut.newValue(TimeUnit.MINUTES.toMillis(19) + TimeUnit.SECONDS.toMillis(59));
        sut.newValue(TimeUnit.MINUTES.toMillis(20));

        Mockito.verify(view, Mockito.times(3)).render(capture(argumentCaptor))
        val counterState = argumentCaptor.value as UiCounterViewState
        Assert.assertEquals(true, counterState.minuteLeftAnimate)
        Assert.assertEquals(true, counterState.minuteRightAnimate)
        Assert.assertEquals(true, counterState.secondLeftAnimate)
        Assert.assertEquals(true, counterState.secondRightAnimate)
    }
}