package com.loyayz.zeus;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class EntityId implements ValueObject {

    public static <T> EntityId of(T id) {
        return new EntityId(id);
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

    private EntityId(Object id) {
        this.id = id;
    }

}
