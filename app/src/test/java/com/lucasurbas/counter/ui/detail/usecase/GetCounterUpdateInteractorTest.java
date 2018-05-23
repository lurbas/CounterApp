package com.lucasurbas.counter.ui.detail.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import rx.TestInteractorSchedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class GetCounterUpdateInteractorTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1 = Counter.builder().id(ID_1).value(0).build();

    @Mock
    private CounterRepository counterRepository;

    private InteractorSchedulers schedulers = new TestInteractorSchedulers();

    private GetCounterUpdateInteractor sut;

    @Before
    public void setup() {
        sut = new GetCounterUpdateInteractor(counterRepository, schedulers);
    }

    @Test
    public void getCounterUpdates() {
        given(counterRepository.getCounterUpdatesWithId(ID_1)).willReturn(Observable.just(COUNTER_1));

        TestObserver<Counter> testObserver = sut.getCounterUpdates(ID_1).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        Counter emittedCounter = testObserver.values().get(0);
        assertEquals(COUNTER_1, emittedCounter);
    }

}