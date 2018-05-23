package rx;

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestInteractorSchedulers implements InteractorSchedulers {

    @Override
    public Scheduler getBackgroundScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler getMainThreadScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler getSingleScheduler() {
        return Schedulers.trampoline();
    }
}
