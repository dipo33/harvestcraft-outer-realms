package com.dipo33.bewitched.data;

import java.util.function.Supplier;

public class ObjectHolder<T> implements Supplier<T> {

    private Supplier<? extends T> delegate;
    private T value;
    private boolean initialized;

    public ObjectHolder(Supplier<? extends T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        if (!this.initialized) {
            this.value = this.delegate.get();
            this.initialized = true;
            delegate = null;
        }

        return value;
    }
}
