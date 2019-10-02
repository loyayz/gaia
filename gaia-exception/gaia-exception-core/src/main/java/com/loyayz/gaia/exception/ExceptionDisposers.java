package com.loyayz.gaia.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionDisposers {
    private static Map<Class<? extends Throwable>, ExceptionDisposer> mapped = new HashMap<>(16);
    private static Map<Class<? extends Throwable>, ExceptionDisposer> lookupCache = new HashMap<>(16);
    private static ExceptionDisposer defaultExceptionDisposer = new AbstractExceptionDisposer() {
    };

    public synchronized static void addExceptionDisposer(ExceptionDisposer disposer) {
        addExceptionDisposers(Collections.singletonList(disposer));
    }

    public synchronized static void addExceptionDisposers(List<ExceptionDisposer> disposers) {
        if (disposers == null) {
            return;
        }
        for (ExceptionDisposer disposer : disposers) {
            addMappedDisposer(disposer);
        }
        lookupCache.clear();
    }

    /**
     * Find a {@link ExceptionDisposer} to handle the given Throwable.
     * Use {@link ExceptionDepthComparator} if more than one match is found.
     *
     * @param exception the exception
     * @return a {@link ExceptionDisposer} to handle the exception, or {@link #defaultExceptionDisposer} if none found
     */
    public static ExceptionDisposer resolveByException(Throwable exception) {
        ExceptionDisposer result = null;
        while (exception != null) {
            result = resolveByExceptionType(exception.getClass());
            if (result != null) {
                break;
            }
            exception = exception.getCause();
        }
        return result == null ? defaultExceptionDisposer : result;
    }

    /**
     * Find a {@link ExceptionDisposer} to handle the given Throwable.
     * Use {@link ExceptionDepthComparator} if more than one match is found.
     *
     * @param exceptionType the exception type
     * @return a ExceptionDisposer to handle the exception, or {@code null} if none found
     */
    public static ExceptionDisposer resolveByExceptionType(Class<? extends Throwable> exceptionType) {
        ExceptionDisposer result = lookupCache.get(exceptionType);
        if (result == null) {
            result = getMappedDisposer(exceptionType);
            lookupCache.put(exceptionType, result);
        }
        return result;
    }

    private static ExceptionDisposer getMappedDisposer(Class<? extends Throwable> exceptionType) {
        ExceptionDisposer result = mapped.get(exceptionType);
        if (result != null) {
            return result;
        }
        List<Class<? extends Throwable>> matches = new ArrayList<>();
        for (Class<? extends Throwable> mappedException : mapped.keySet()) {
            if (mappedException.isAssignableFrom(exceptionType)) {
                matches.add(mappedException);
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        matches.sort(new ExceptionDepthComparator(exceptionType));
        return mapped.get(matches.get(0));
    }

    private static void addMappedDisposer(ExceptionDisposer disposer) {
        for (Class<? extends Throwable> exception : disposer.exceptions()) {
            ExceptionDisposer store = disposer;
            // 同类型，根据 @Order 排序
            if (mapped.containsKey(exception)) {
                ExceptionDisposer old = mapped.get(exception);
                store = AnnotationAwareOrderComparator.INSTANCE
                        .compare(old, disposer) < 0 ? old : disposer;
            }
            mapped.put(exception, store);
        }
    }

}
