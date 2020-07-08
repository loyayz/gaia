package com.loyayz.gaia.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * 转换 list
     */
    public static <T, R> List<R> convert(List<T> origin, Function<T, R> mapper) {
        if (origin == null) {
            return new ArrayList<>();
        }
        return origin.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 转换 bean
     */
    public static <T, R> R convert(T origin, Function<T, R> mapper) {
        if (origin == null) {
            return null;
        }
        return mapper.apply(origin);
    }

}
