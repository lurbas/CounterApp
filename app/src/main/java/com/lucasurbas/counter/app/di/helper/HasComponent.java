package com.lucasurbas.counter.app.di.helper;

import android.support.annotation.NonNull;

public interface HasComponent<C> {

    /**
     * @return Component provided by this class.
     */
    @NonNull
    C getComponent();

}
