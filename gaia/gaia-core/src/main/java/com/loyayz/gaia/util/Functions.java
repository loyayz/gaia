package com.loyayz.gaia.util;

import java.util.function.Consumer;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class Functions {

    public static <T> void execute(Consumer<T> consumer, T obj) {
        consumer.accept(obj);
    }

    public static <T> void executeIfNotNull(Consumer<T> consumer, T obj) {
        if (obj != null) {
            consumer.accept(obj);
        }
    }

}
