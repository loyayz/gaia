package com.loyayz.zeus;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Identity implements ValueObject {

    public static <T> Identity of(T id) {
        return new Identity(id);
    }

    private Object id;

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) this.id;
    }

    public <T> void set(T id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return this.id == null;
    }

    private Identity(Object id) {
        this.id = id;
    }

}
