package com.lucasurbas.counter.service.di;

import com.lucasurbas.counter.app.di.scope.ServiceScope;
import com.lucasurbas.counter.service.RunningCounterService;

import dagger.Subcomponent;

@Subcomponent(modules = {
        RunningCounterServiceModule.class
})
@ServiceScope
public interface RunningCounterServiceComponent {

    void inject(RunningCounterService service);
}
