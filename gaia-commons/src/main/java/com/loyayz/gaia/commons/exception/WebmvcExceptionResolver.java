package com.loyayz.gaia.commons.exception;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class WebmvcExceptionResolver {
    @Setter
    private ExceptionLoggerStrategy loggerStrategy = ExceptionLoggerStrategy.DEFAULT_STRATEGY;

    public ExceptionResult handlerException(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        ExceptionDisposer disposer = ExceptionDisposers.resolveByException(exception);
        this.writeLog(request, exception, disposer);

        ExceptionResult result = disposer.getResult(exception);
        response.setStatus(result.getStatus());
        return result;
    }

    private void writeLog(HttpServletRequest request, Throwable exception, ExceptionDisposer disposer) {
        String apiUrl = request.getMethod() + "-" + request.getRequestURI();
        this.loggerStrategy.write(log, apiUrl, exception, disposer);
    }

}
