package com.loyayz.gaia.exception.helper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultExceptionLogger implements ExceptionLogger {

    @Override
    public void write(ExceptionLoggerParam param) {
        Throwable exception = param.getException();
        String logLevel = param.getLogLevel();
        String logMsg = param.getLogMessage();
        if ("TRACE".equalsIgnoreCase(logLevel)) {
            if (log.isTraceEnabled()) {
                log.trace(logMsg, exception);
            }
        } else if ("DEBUG".equalsIgnoreCase(logLevel)) {
            if (log.isDebugEnabled()) {
                log.debug(logMsg, exception);
            }
        } else if ("INFO".equalsIgnoreCase(logLevel)) {
            if (log.isInfoEnabled()) {
                log.info(logMsg, exception);
            }
        } else if ("WARN".equalsIgnoreCase(logLevel)) {
            if (log.isWarnEnabled()) {
                log.warn(logMsg, exception);
            }
        } else if ("ERROR".equalsIgnoreCase(logLevel)) {
            if (log.isErrorEnabled()) {
                log.error(logMsg, exception);
            }
        }
    }

}
