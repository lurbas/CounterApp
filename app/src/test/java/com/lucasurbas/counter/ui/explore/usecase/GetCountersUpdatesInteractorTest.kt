package com.lucasurbas.counter.ui.explore.usecase

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import rx.TestInteractorSchedulers
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class GetCountersUpdatesInteractorTest {

    companion object {
        private val ID_1 = 1
        private val COUNTER_1 = Counter(id = ID_1, value = 0)
        private val COUNTER_LIST = Arrays.asList(COUNTER_1)
    }

    @Mock
    lateinit var counterRepository: CounterRepository

    val schedulers = TestInteractorSchedulers()

    lateinit var sut: GetCountersUpdatesInteractor

    @Before
    fun setup() {
        sut = GetCountersUpdatesInteractor(counterRepository, schedulers)
    }

    @Test
    fun getCountersUpdates() {
        given(counterRepository.getCountersUpdates()).willReturn(Observable.just(COUNTER_LIST))

        val testObserver = sut.getCountersUpdates().test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertEquals(1, testObserver.valueCount())
        val emittedCounterList = testObserver.values()[0]
        assertEquals(1, emittedCounterList.size)
        assertEquals(COUNTER_1, emittedCounterList[0])
    }
}