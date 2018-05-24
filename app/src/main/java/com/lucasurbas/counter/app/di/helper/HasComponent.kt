package com.lucasurbas.counter.app.di.helper

interface HasComponent<C> {

    /**
     * @return Component provided by this class.
     */
    var component: C?

}
