package com.loyayz.gaia.commons.exception;

import org.slf4j.Logger;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@FunctionalInterface
public interface ExceptionLoggerStrategy {

    /**
     * write log
     */
    void write(Logger logger, String apiUrl, Throwable exception, ExceptionDisposer disposer);

    ExceptionLoggerStrategy DEFAULT_STRATEGY = (logger, apiUrl, exception, disposer) -> {
        if (logger.isWarnEnabled()) {
            String msg = "[" + apiUrl + "]  " + disposer.getResult(exception);
            logger.warn(msg, exception);
        } else if (logger.isDebugEnabled()) {
            String msg = "[" + apiUrl + "]  " + disposer.getResult(exception);
            logger.debug(msg, exception);
        }
    };

}
