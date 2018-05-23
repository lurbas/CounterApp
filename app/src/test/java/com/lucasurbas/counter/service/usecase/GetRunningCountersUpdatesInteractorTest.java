package com.lucasurbas.counter.service.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import rx.TestInteractorSchedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class GetRunningCountersUpdatesInteractorTest {

    private static final int ID_1 = 1;
    private static final int ID_2 = 2;
    private static final Counter COUNTER_1 = Counter.builder().id(ID_1).value(0).build();
    private static final Counter COUNTER_2_RUNNING = Counter.builder().id(ID_2).isRunning(true).value(0).build();
    private static final List<Counter> COUNTER_LIST = Arrays.asList(COUNTER_1, COUNTER_2_RUNNING);

    @Mock
    private CounterRepository counterRepository;

    private InteractorSchedulers schedulers = new TestInteractorSchedulers();

    private GetRunningCountersUpdatesInteractor sut;

    @Before
    public void setup() {
        sut = new GetRunningCountersUpdatesInteractor(counterRepository, schedulers);
    }

    @Test
    public void getRunningCountersUpdates() {
        given(counterRepository.getCountersUpdates()).willReturn(Observable.just(COUNTER_LIST));

        TestObserver<List<Counter>> testObserver = sut.getRunningCountersUpdates().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        List<Counter> emittedCounterList = testObserver.values().get(0);
        assertEquals(1, emittedCounterList.size());
        assertEquals(COUNTER_2_RUNNING, emittedCounterList.get(0));
    }

}