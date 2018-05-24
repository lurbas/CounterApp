package com.lucasurbas.counter.service.usecase

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
class GetRunningCountersUpdatesInteractorTest {

    companion object {
        private val ID_1 = 1
        private val ID_2 = 2
        private val COUNTER_1 = Counter(ID_1, 0, false)
        private val COUNTER_2_RUNNING = Counter(ID_2, 0, true)
        private val COUNTER_LIST = Arrays.asList(COUNTER_1, COUNTER_2_RUNNING)
    }

    @Mock
    lateinit var counterRepository: CounterRepository

    val schedulers = TestInteractorSchedulers()

    lateinit var sut: GetRunningCountersUpdatesInteractor

    @Before
    fun setup() {
        sut = GetRunningCountersUpdatesInteractor(counterRepository, schedulers)
    }

    @Test
    fun getRunningCountersUpdates() {
        given(counterRepository.getCountersUpdates()).willReturn(Observable.just(COUNTER_LIST))

        val testObserver = sut.getRunningCountersUpdates().test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertEquals(1, testObserver.valueCount())
        val emittedCounterList = testObserver.values()[0]
        assertEquals(1, emittedCounterList.size)
        assertEquals(COUNTER_2_RUNNING, emittedCounterList[0])
    }
}