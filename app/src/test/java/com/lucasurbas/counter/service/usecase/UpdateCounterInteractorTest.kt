package com.lucasurbas.counter.service.usecase

import com.lucasurbas.counter.data.model.Counter
import com.lucasurbas.counter.data.repository.CounterRepository
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import rx.TestInteractorSchedulers

@RunWith(MockitoJUnitRunner::class)
class UpdateCounterInteractorTest {

    companion object {
        private val ID_1 = 1
        private val COUNTER_1_RUNNING = Counter(ID_1, 0, true)
    }
    
    @Mock
    lateinit var counterRepository: CounterRepository

    val schedulers = TestInteractorSchedulers()

    lateinit var sut: UpdateCounterInteractor

    @Before
    fun setup() {
        sut = UpdateCounterInteractor(counterRepository, schedulers)
    }

    @Test
    fun updateCounter() {
        given(counterRepository.updateCounter(COUNTER_1_RUNNING)).willReturn(Completable.complete())

        val testObserver = sut.updateCounter(COUNTER_1_RUNNING).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
    }
}