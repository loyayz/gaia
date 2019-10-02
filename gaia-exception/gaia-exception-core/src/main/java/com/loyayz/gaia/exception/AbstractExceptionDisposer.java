package com.loyayz.gaia.exception;

import lombok.Data;
import org.springframework.core.ExceptionDepthComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractExceptionDisposer implements ExceptionDisposer {
    private Container defaultContainer;
    private Map<Class<? extends Throwable>, Container> mapped = new HashMap<>(16);
    private Map<Class<? extends Throwable>, Container> lookupCache = new HashMap<>(16);

    protected AbstractExceptionDisposer() {
        this(ExceptionDisposer.DEFAULT_CODE);
    }

    protected AbstractExceptionDisposer(String defaultCode) {
        this(defaultCode, ExceptionDisposer.DEFAULT_STATUS);
    }

    protected AbstractExceptionDisposer(String defaultCode, int defaultStatus) {
        this(defaultCode, defaultStatus, ExceptionDisposer.DEFAULT_LEVEL);
    }

    protected AbstractExceptionDisposer(String defaultCode, int defaultStatus, int defaultLevel) {
        this(new Container(defaultCode, defaultStatus, defaultLevel));
    }

    protected AbstractExceptionDisposer(Container container) {
        this.defaultContainer = container;
    }

    protected AbstractExceptionDisposer addException(Class<? extends Throwable> e, Container container) {
        this.mapped.put(e, container);
        return this;
    }

    protected AbstractExceptionDisposer addException(Class<? extends Throwable> e) {
        return this.addException(e, this.defaultContainer);
    }

    protected AbstractExceptionDisposer addException(Class<? extends Throwable> e, String code) {
        return this.addException(e, new Container(code));
    }

    protected AbstractExceptionDisposer addException(Class<? extends Throwable> e, String code, int status) {
        return this.addException(e, new Container(code, status));
    }

    protected AbstractExceptionDisposer addException(Class<? extends Throwable> e, String code, int status, int level) {
        return this.addException(e, new Container(code, status, level));
    }

    @Override
    public List<Class<? extends Throwable>> exceptions() {
        return new ArrayList<>(this.mapped.keySet());
    }

    @Override
    public String code(Throwable e) {
        String result = this.resolveByException(e).getCode();
        return (result == null || "".equals(result.trim())) ?
                this.defaultContainer.getCode() : result;
    }

    @Override
    public int status(Throwable e) {
        Integer result = this.resolveByException(e).getStatus();
        return result == null ? this.defaultContainer.getStatus() : result;
    }

    @Override
    public int level(Throwable e) {
        Integer result = this.resolveByException(e).getLevel();
        return result == null ? this.defaultContainer.getLevel() : result;
    }

    protected Container resolveByException(Throwable exception) {
        Container result = null;
        while (exception != null) {
            result = resolveByExceptionType(exception.getClass());
            if (result != null) {
                break;
            }
            exception = exception.getCause();
        }
        return result == null ? this.defaultContainer : result;
    }

    protected Container resolveByExceptionType(Class<? extends Throwable> exceptionType) {
        Container result = this.lookupCache.get(exceptionType);
        if (result == null) {
            result = this.getMappedContainer(exceptionType);
            this.lookupCache.put(exceptionType, result);
        }
        return result;
    }

    private Container getMappedContainer(Class<? extends Throwable> exceptionType) {
        Container result = this.mapped.get(exceptionType);
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
        return this.mapped.get(matches.get(0));
    }

    @Data
    protected static class Container {
        private String code;
        private Integer status;
        private Integer level;

        public Container(String code) {
            this.code = code;
        }

        public Container(String code, Integer status) {
            this.code = code;
            this.status = status;
        }

        public Container(String code, Integer status, Integer level) {
            this.code = code;
            this.status = status;
            this.level = level;
        }
    }

}
