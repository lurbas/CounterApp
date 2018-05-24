package com.lucasurbas.counter.service.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import rx.TestInteractorSchedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class GetCounterInteractorTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1 = new Counter(ID_1, 0, false);

    @Mock
    private CounterRepository counterRepository;

    private InteractorSchedulers schedulers = new TestInteractorSchedulers();

    private GetCounterInteractor sut;

    @Before
    public void setup() {
        sut = new GetCounterInteractor(counterRepository, schedulers);
    }

    @Test
    public void getCounter() {
        given(counterRepository.getCounterWithId(ID_1)).willReturn(Single.just(COUNTER_1));

        TestObserver<Counter> testObserver = sut.getCounter(ID_1).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        Counter emittedCounter = testObserver.values().get(0);
        assertEquals(COUNTER_1, emittedCounter);
    }

}