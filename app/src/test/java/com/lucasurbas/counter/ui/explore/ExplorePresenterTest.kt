package com.lucasurbas.counter.ui.explore

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.ui.explore.mapper.UiCounterItemMapper
import com.lucasurbas.counter.ui.explore.usecase.GetCountersUpdatesInteractor
import com.nhaarman.mockito_kotlin.capture
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
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ExplorePresenterTest {

    companion object {
        private val ID_1 = 1
        private val COUNTER_1 = Counter(ID_1, 0, false)
        private val COUNTER_LIST = Arrays.asList(COUNTER_1)
    }

    @Mock
    lateinit var getCountersUpdatesInteractor: GetCountersUpdatesInteractor
    @Mock
    lateinit var view: ExplorePresenter.View
    @Captor
    lateinit var argumentCaptor: ArgumentCaptor<UiExploreState>

    val uiCounterItemMapper = UiCounterItemMapper()
    lateinit var subject: PublishSubject<List<Counter>>

    lateinit var sut: ExplorePresenter

    @Before
    fun setup() {
        subject = PublishSubject.create()
        given(getCountersUpdatesInteractor.getCountersUpdates()).willReturn(subject)
        sut = ExplorePresenter(getCountersUpdatesInteractor, uiCounterItemMapper)
    }

    @After
    fun tearDown() {
        sut.onCleared()
    }

    @Test
    fun firstAttachViewEmitsInitialState() {

        sut.attachView(view)

        verify(view).render(UiExploreState())
    }

    @Test
    fun emitNewStateWhenCounterListUpdated() {
        sut.attachView(view)

        subject.onNext(COUNTER_LIST)

        verify(view, times(2)).render(capture(argumentCaptor))
        val (itemList) = argumentCaptor.value
        assertEquals(2, itemList.size)
        assertEquals(ID_1, itemList[1].id)
    }

    @Test
    fun dontEmitItemsIfViewNotAttached() {

        subject.onNext(COUNTER_LIST)

        verify(view, never()).render(capture(argumentCaptor))
    }

    @Test
    fun dontEmitItemsIfViewDetached() {
        sut.attachView(view)
        sut.detachView()

        subject.onNext(COUNTER_LIST)

        verify(view, times(1)).render(UiExploreState())
    }

    @Test
    fun emitLastStateWhenViewReAttached() {
        subject.onNext(COUNTER_LIST)

        sut.attachView(view)

        verify(view, times(1)).render(capture(argumentCaptor))
        val (itemList) = argumentCaptor.value
        assertEquals(2, itemList.size)
        assertEquals(ID_1, itemList[1].id)
    }
}