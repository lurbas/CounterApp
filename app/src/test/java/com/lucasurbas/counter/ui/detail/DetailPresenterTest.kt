package com.lucasurbas.counter.ui.detail

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.service.usecase.GetCounterInteractor
import com.lucasurbas.counter.service.usecase.UpdateCounterInteractor
import com.lucasurbas.counter.ui.detail.mapper.UiCounterDetailMapper
import com.lucasurbas.counter.ui.detail.usecase.GetCounterUpdateInteractor
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
import org.mockito.BDDMockito.given
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailPresenterTest {

    companion object {
        private val ID_1 = 1
        private val COUNTER_1 = Counter(ID_1, 0, false)
        private val COUNTER_1_MILLIS = Counter(ID_1, 100, false)
    }

    @Mock
    lateinit var getCounterUpdateInteractor: GetCounterUpdateInteractor
    @Mock
    lateinit var getCounterInteractor: GetCounterInteractor
    @Mock
    lateinit var updateCounterInteractor: UpdateCounterInteractor
    @Mock
    lateinit var view: DetailPresenter.View
    @Captor
    lateinit var argumentCaptor: ArgumentCaptor<UiDetailState>

    val uiCounterDetailMapper = UiCounterDetailMapper()
    lateinit var subject: PublishSubject<Counter>

    lateinit var sut: DetailPresenter

    @Before
    fun setup() {
        subject = PublishSubject.create()
        given(getCounterUpdateInteractor.getCounterUpdates(ID_1)).willReturn(subject)
        sut = DetailPresenter(getCounterUpdateInteractor,
                getCounterInteractor,
                updateCounterInteractor,
                uiCounterDetailMapper)
    }

    @After
    fun tearDown() {
        sut.onCleared()
    }

    @Test
    fun firstAttachViewEmitsInitialState() {

        sut.attachView(view)

        verify(view).render(UiDetailState())
    }

    @Test
    fun dontEmitItemsIfViewNotAttached() {
        subject.onNext(COUNTER_1)

        verify<DetailPresenter.View>(view, never()).render(capture(argumentCaptor))
    }

    @Test
    fun getCounterDoesntEmitItemsIfViewNotAttached2() {
        subject.onNext(COUNTER_1)

        sut.getCounter(ID_1)

        verify(view, never()).render(capture(argumentCaptor))
    }

    @Test
    fun emitNewStateWhenCounterUpdated() {
        sut.attachView(view)
        sut.getCounter(ID_1)

        subject.onNext(COUNTER_1)

        verify(view, times(2)).render(capture(argumentCaptor))
        val (counter) = argumentCaptor.value
        assertEquals(ID_1, counter!!.id)
    }

    @Test
    fun updateInitialValueTriggersCounterUpdate() {
        given(getCounterInteractor.getCounter(ID_1)).willReturn(Single.just(COUNTER_1))
        `when`(updateCounterInteractor.updateCounter(COUNTER_1_MILLIS)).thenAnswer {
            subject.onNext(COUNTER_1_MILLIS)
            Completable.complete()
        }
        sut.attachView(view)
        sut.getCounter(ID_1)
        subject.onNext(COUNTER_1)

        sut.updateInitialValue(ID_1, 100)

        verify(view, times(3)).render(capture(argumentCaptor))
        val (counter) = argumentCaptor.value
        assertEquals(100, counter!!.valueInMillis)
    }
}