package com.lucasurbas.counter.service.usecase;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;
import com.lucasurbas.counter.data.model.Counter;
import com.lucasurbas.counter.data.repository.CounterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import rx.TestInteractorSchedulers;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCounterInteractorTest {

    private static final int ID_1 = 1;
    private static final Counter COUNTER_1_RUNNING = Counter.builder().id(ID_1).isRunning(true).value(0).build();

    @Mock
    private CounterRepository counterRepository;

    private InteractorSchedulers schedulers = new TestInteractorSchedulers();

    private UpdateCounterInteractor sut;

    @Before
    public void setup() {
        sut = new UpdateCounterInteractor(counterRepository, schedulers);
    }

    @Test
    public void updateCounter() {
        given(counterRepository.updateCounter(COUNTER_1_RUNNING)).willReturn(Completable.complete());

        TestObserver<Void> testObserver = sut.updateCounter(COUNTER_1_RUNNING).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
    }

}