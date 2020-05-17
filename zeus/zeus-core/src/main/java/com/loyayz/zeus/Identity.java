package com.loyayz.zeus;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class Identity<T> implements ValueObject {

    private T id;

    public T get() {
        return this.id;
    }

    public void set(T id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return this.id == null;
    }

    protected Identity(T id) {
        this.id = id;
    }

}
