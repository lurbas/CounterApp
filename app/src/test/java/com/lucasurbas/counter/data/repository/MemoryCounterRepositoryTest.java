package com.lucasurbas.counter.data.repository;

import com.lucasurbas.counter.data.model.Counter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MemoryCounterRepositoryTest {

    private static final int ID_1 = 1;
    private static final int ID_2 = 2;

    private static final Counter COUNTER_1 = new Counter(ID_1, 0, false);
    private static final Counter COUNTER_1_RUNNING = new Counter(ID_1, 0, true);
    private static final Counter COUNTER_2 = new Counter(ID_2, 0, false);

    private MemoryCounterRepository sut;

    @Before
    public void setup() {
        sut = new MemoryCounterRepository();
    }

    @Test
    public void updateCounterCompletes() {

        TestObserver<Void> testSubscriber = sut.updateCounter(COUNTER_1).test();

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
    }

    @Test
    public void updateCounterEmitsUpdate() {
        List<Counter> counterList = new ArrayList<>();
        counterList.add(COUNTER_1);
        sut.initData(counterList);
        TestObserver<List<Counter>> testObserver = sut.getCountersUpdates().test();

        sut.updateCounter(COUNTER_1_RUNNING).test();

        testObserver.assertNoErrors();
        assertEquals(2, testObserver.valueCount());
        List<Counter> emittedCounterList = testObserver.values().get(0);
        assertEquals(1, emittedCounterList.size());
        assertEquals(COUNTER_1_RUNNING, emittedCounterList.get(0));
    }

    @Test
    public void updateCounterDoesntTouchOtherEntities() {
        List<Counter> counterList = new ArrayList<>();
        counterList.add(COUNTER_1);
        counterList.add(COUNTER_2);
        sut.initData(counterList);
        TestObserver<List<Counter>> testObserver = sut.getCountersUpdates().test();

        sut.updateCounter(COUNTER_1_RUNNING).test();

        testObserver.assertNoErrors();
        assertEquals(2, testObserver.valueCount());
        List<Counter> emittedCounterList = testObserver.values().get(1);
        assertEquals(2, emittedCounterList.size());
        assertEquals(COUNTER_1_RUNNING, emittedCounterList.get(0));
        assertEquals(COUNTER_2, emittedCounterList.get(1));
    }

    @Test
    public void getCountersReturnsAllEntities() {
        List<Counter> counterList = new ArrayList<>();
        counterList.add(COUNTER_1);
        counterList.add(COUNTER_2);
        sut.initData(counterList);

        TestObserver<List<Counter>> testObserver = sut.getCounters().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        List<Counter> emittedCounterList = testObserver.values().get(0);
        assertEquals(2, emittedCounterList.size());
        assertEquals(COUNTER_1, emittedCounterList.get(0));
        assertEquals(COUNTER_2, emittedCounterList.get(1));
    }

    @Test
    public void getCounterWithIdReturnsCorrectEntity() {
        List<Counter> counterList = new ArrayList<>();
        counterList.add(COUNTER_1);
        counterList.add(COUNTER_2);
        sut.initData(counterList);

        TestObserver<Counter> testObserver = sut.getCounterWithId(ID_2).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        Counter emittedCounter = testObserver.values().get(0);
        assertEquals(COUNTER_2, emittedCounter);
    }
}