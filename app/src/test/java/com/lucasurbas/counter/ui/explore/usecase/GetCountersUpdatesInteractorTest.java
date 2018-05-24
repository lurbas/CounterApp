package com.lucasurbas.counter.ui.explore.usecase;

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
public class GetCountersUpdatesInteractorTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1 = new Counter(ID_1, 0, false);
    private static final List<Counter> COUNTER_LIST = Arrays.asList(COUNTER_1);

    @Mock
    private CounterRepository counterRepository;

    private InteractorSchedulers schedulers = new TestInteractorSchedulers();

    private GetCountersUpdatesInteractor sut;

    @Before
    public void setup() {
        sut = new GetCountersUpdatesInteractor(counterRepository, schedulers);
    }

    @Test
    public void getCountersUpdates() {
        given(counterRepository.getCountersUpdates()).willReturn(Observable.just(COUNTER_LIST));

        TestObserver<List<Counter>> testObserver = sut.getCountersUpdates().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        List<Counter> emittedCounterList = testObserver.values().get(0);
        assertEquals(1, emittedCounterList.size());
        assertEquals(COUNTER_1, emittedCounterList.get(0));
    }

}