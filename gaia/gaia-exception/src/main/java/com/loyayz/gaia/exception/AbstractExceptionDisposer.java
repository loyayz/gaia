package com.loyayz.gaia.exception;

import lombok.Data;
import org.springframework.core.ExceptionDepthComparator;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        this.doAddExceptions();
    }

    /**
     * 初始化异常处理
     */
    protected abstract void doAddExceptions();

    protected AbstractExceptionDisposer() {
        this(ExceptionDisposer.DEFAULT_CODE);
    }

    protected AbstractExceptionDisposer(String defaultCode) {
        this(defaultCode, ExceptionDisposer.DEFAULT_STATUS);
    }

    protected AbstractExceptionDisposer(String defaultCode, int defaultStatus) {
        this(defaultCode, defaultStatus, null);
    }

    protected AbstractExceptionDisposer(String defaultCode, int defaultStatus, String defaultMessage) {
        this(defaultCode, defaultStatus, defaultMessage, ExceptionDisposer.DEFAULT_LOG_LEVEL);
    }

    protected AbstractExceptionDisposer(String defaultCode, int defaultStatus, String defaultMessage, String defaultLogLevel) {
        this(new Container(defaultCode, defaultStatus, defaultMessage, defaultLogLevel));
    }

    protected void addException(Class<? extends Throwable> e) {
        this.addException(e, this.defaultContainer);
    }

    protected void addException(Class<? extends Throwable> e, String code) {
        this.addException(e, new Container(code));
    }

    protected void addException(Class<? extends Throwable> e, String code, int status) {
        this.addException(e, new Container(code, status));
    }

    protected void addException(Class<? extends Throwable> e, String code, int status, String message) {
        this.addException(e, new Container(code, status, message));
    }

    protected void addException(Class<? extends Throwable> e, String code, int status, String message, String logLevel) {
        this.addException(e, new Container(code, status, message, logLevel));
    }

    protected void addException(Class<? extends Throwable> e, Container container) {
        this.mapped.put(e, container);
    }

    private AbstractExceptionDisposer(Container container) {
        this.defaultContainer = container;
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
    public String message(Throwable e) {
        String result = this.resolveByException(e).getMessage();
        result = (result == null || "".equals(result.trim())) ?
                this.defaultContainer.getMessage() : result;
        return (result == null || "".equals(result.trim())) ?
                e.getMessage() : result;
    }

    @Override
    public int status(Throwable e) {
        Integer result = this.resolveByException(e).getStatus();
        return result == null ? this.defaultContainer.getStatus() : result;
    }

    @Override
    public String logLevel(Throwable e) {
        String result = this.resolveByException(e).getLogLevel();
        return result == null ? this.defaultContainer.getLogLevel() : result;
    }

    private Container resolveByException(Throwable exception) {
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

    private Container resolveByExceptionType(Class<? extends Throwable> exceptionType) {
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
        private String message;
        private String logLevel;

        Container(String code) {
            this(code, null);
        }

        Container(String code, Integer status) {
            this(code, status, null);
        }

        Container(String code, Integer status, String message) {
            this(code, status, message, null);
        }

        Container(String code, Integer status, String message, String logLevel) {
            this.code = code;
            this.status = status;
            this.message = message;
            this.logLevel = logLevel;
        }
    }

}
