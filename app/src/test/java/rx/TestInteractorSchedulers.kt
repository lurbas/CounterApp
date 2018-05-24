package rx

import com.lucasurbas.counter.app.di.rx.InteractorSchedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestInteractorSchedulers : InteractorSchedulers {

    override val backgroundScheduler: Scheduler
        get() = Schedulers.trampoline()

    override val mainThreadScheduler: Scheduler
        get() = Schedulers.trampoline()

    override val singleScheduler: Scheduler
        get() = Schedulers.trampoline()
}
