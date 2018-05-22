package rx;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import com.lucasurbas.counter.rx.InteractorSchedulers;

public class TestInteractorSchedulers implements InteractorSchedulers {

    @Override
    public Scheduler getBackgroundScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler getMainThreadScheduler() {
        return Schedulers.trampoline();
    }
}
