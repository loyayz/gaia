package com.loyayz.gaia.exception.core;

import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class ExceptionResolver {
    private final Map<Class<? extends Throwable>, ExceptionDefiner> mapped = new HashMap<>(16);
    private final Map<Class<? extends Throwable>, ExceptionDefiner> lookupCache = new HashMap<>(16);

    public ExceptionResolver(List<ExceptionDefiner> definers) {
        if (definers == null) {
            return;
        }
        for (ExceptionDefiner definer : definers) {
            this.addMappedDefiner(definer);
        }
    }

    public ExceptionResult getExceptionResult(Throwable exception) {
        ExceptionDefiner handler = this.resolveByException(exception);
        Optional<ExceptionResult> result =
                (handler == null) ? Optional.empty() :
                        Optional.of(handler.getResult(exception));
        return result.orElse(ExceptionResult.defaultResponse(exception));
    }

    /**
     * Find a {@link ExceptionDefiner} to handle the given Throwable.
     * Use {@link ExceptionDepthComparator} if more than one match is found.
     *
     * @param exception the exception
     * @return a ExceptionDisposer to handle the exception, or {@code null} if none found
     */
    public ExceptionDefiner resolveByException(Throwable exception) {
        ExceptionDefiner result = null;
        while (exception != null) {
            result = this.resolveByExceptionType(exception.getClass());
            if (result != null) {
                break;
            }
            exception = exception.getCause();
        }
        return result;
    }

    /**
     * Find a {@link ExceptionDefiner} to handle the given Throwable.
     * Use {@link ExceptionDepthComparator} if more than one match is found.
     *
     * @param exceptionType the exception type
     * @return a ExceptionDisposer to handle the exception, or {@code null} if none found
     */
    public ExceptionDefiner resolveByExceptionType(Class<? extends Throwable> exceptionType) {
        ExceptionDefiner disposer = this.lookupCache.get(exceptionType);
        if (disposer == null) {
            disposer = getMappedDisposer(exceptionType);
            this.lookupCache.put(exceptionType, disposer);
        }
        return disposer;
    }

    private ExceptionDefiner getMappedDisposer(Class<? extends Throwable> exceptionType) {
        List<Class<? extends Throwable>> matches = new ArrayList<>();
        for (Class<? extends Throwable> mappedException : this.mapped.keySet()) {
            if (mappedException.isAssignableFrom(exceptionType)) {
                matches.add(mappedException);
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        matches.sort(new ExceptionDepthComparator(exceptionType));
        return this.mapped.get(matches.get(0));
    }

    private void addMappedDefiner(ExceptionDefiner disposers) {
        for (Class<? extends Throwable> exception : disposers.exceptions()) {
            ExceptionDefiner storeHandler = disposers;
            // 同类型，根据 @Order 排序
            if (this.mapped.containsKey(exception)) {
                ExceptionDefiner oldDisposer = this.mapped.get(exception);
                storeHandler = AnnotationAwareOrderComparator.INSTANCE
                        .compare(oldDisposer, disposers) < 0 ? oldDisposer : disposers;
            }
            this.mapped.put(exception, storeHandler);
        }
    }

}
