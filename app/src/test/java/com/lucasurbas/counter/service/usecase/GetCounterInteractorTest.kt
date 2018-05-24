package com.lucasurbas.counter.service.usecase

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import rx.TestInteractorSchedulers

@RunWith(MockitoJUnitRunner::class)
class GetCounterInteractorTest {

    companion object {
        private val ID_1 = 1
        private val COUNTER_1 = Counter(ID_1, 0, false)
    }

    @Mock
    lateinit var counterRepository: CounterRepository

    val schedulers = TestInteractorSchedulers()

    lateinit var sut: GetCounterInteractor

    @Before
    fun setup() {
        sut = GetCounterInteractor(counterRepository, schedulers)
    }

    @Test
    fun getCounter() {
        given(counterRepository.getCounterWithId(ID_1)).willReturn(Single.just(COUNTER_1))

        val testObserver = sut.getCounter(ID_1).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertEquals(1, testObserver.valueCount())
        val emittedCounter = testObserver.values()[0]
        assertEquals(COUNTER_1, emittedCounter)
    }
}