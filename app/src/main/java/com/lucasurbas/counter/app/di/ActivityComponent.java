package com.lucasurbas.counter.app.di;

import dagger.Subcomponent;
import com.lucasurbas.counter.app.di.scope.ActivityScope;

@Subcomponent(modules = {ActivityModule.class})
@ActivityScope
public interface ActivityComponent {

}
