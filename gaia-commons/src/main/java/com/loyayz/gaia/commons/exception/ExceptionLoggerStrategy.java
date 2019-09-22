package com.loyayz.gaia.commons.exception;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@FunctionalInterface
public interface ExceptionLoggerStrategy {

    /**
     * write log
     */
    void write(String apiMethod, String apiUrl, Throwable exception, ExceptionDisposer disposer);

}
