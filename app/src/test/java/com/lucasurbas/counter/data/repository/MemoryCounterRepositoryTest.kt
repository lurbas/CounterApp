package com.lucasurbas.counter.data.repository

import com.lucasurbas.counter.data.model.Counter
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MemoryCounterRepositoryTest {

    companion object {
        private val ID_1 = 1
        private val ID_2 = 2
        private val COUNTER_1 = Counter(ID_1, 0, false)
        private val COUNTER_1_RUNNING = Counter(ID_1, 0, true)
        private val COUNTER_2 = Counter(ID_2, 0, false)
    }

    lateinit var sut: MemoryCounterRepository

    @Before
    fun setup() {
        sut = MemoryCounterRepository()
    }

    @Test
    fun updateCounterCompletes() {

        val testSubscriber = sut.updateCounter(COUNTER_1).test()

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
    }

    @Test
    fun updateCounterEmitsUpdate() {
        val counterList = ArrayList<Counter>()
        counterList.add(COUNTER_1)
        sut.initData(counterList)
        val testObserver = sut.getCountersUpdates().test()

        sut.updateCounter(COUNTER_1_RUNNING).test()

        testObserver.assertNoErrors()
        assertEquals(2, testObserver.valueCount())
        val emittedCounterList = testObserver.values()[0]
        assertEquals(1, emittedCounterList.size)
        assertEquals(COUNTER_1_RUNNING, emittedCounterList[0])
    }

    @Test
    fun updateCounterDoesntTouchOtherEntities() {
        val counterList = ArrayList<Counter>()
        counterList.add(COUNTER_1)
        counterList.add(COUNTER_2)
        sut.initData(counterList)
        val testObserver = sut.getCountersUpdates().test()

        sut.updateCounter(COUNTER_1_RUNNING).test()

        testObserver.assertNoErrors()
        assertEquals(2, testObserver.valueCount())
        val emittedCounterList = testObserver.values()[1]
        assertEquals(2, emittedCounterList.size)
        assertEquals(COUNTER_1_RUNNING, emittedCounterList[0])
        assertEquals(COUNTER_2, emittedCounterList[1])
    }

    @Test
    fun getCountersReturnsAllEntities() {
        val counterList = ArrayList<Counter>()
        counterList.add(COUNTER_1)
        counterList.add(COUNTER_2)
        sut.initData(counterList)

        val testObserver = sut.getCounters().test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertEquals(1, testObserver.valueCount())
        val emittedCounterList = testObserver.values()[0]
        assertEquals(2, emittedCounterList.size)
        assertEquals(COUNTER_1, emittedCounterList[0])
        assertEquals(COUNTER_2, emittedCounterList[1])
    }

    @Test
    fun getCounterWithIdReturnsCorrectEntity() {
        val counterList = ArrayList<Counter>()
        counterList.add(COUNTER_1)
        counterList.add(COUNTER_2)
        sut.initData(counterList)

        val testObserver = sut.getCounterWithId(ID_2).test()

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
        assertEquals(1, testObserver.valueCount())
        val emittedCounter = testObserver.values()[0]
        assertEquals(COUNTER_2, emittedCounter)
    }
}