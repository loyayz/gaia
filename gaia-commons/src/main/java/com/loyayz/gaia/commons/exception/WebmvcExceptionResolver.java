package com.loyayz.gaia.commons.exception;

import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class WebmvcExceptionResolver {
    @Setter
    private ExceptionLoggerStrategy loggerStrategy = new DefaultExceptionLoggerStrategy();

    public ExceptionResult handlerException(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        ExceptionDisposer disposer = ExceptionDisposers.resolveByException(exception);
        this.writeLog(request, exception, disposer);

        ExceptionResult result = disposer.getResult(exception);
        response.setStatus(result.getStatus());
        return result;
    }

    private void writeLog(HttpServletRequest request, Throwable exception, ExceptionDisposer disposer) {
        String apiMethod = request.getMethod();
        String apiUrl = request.getRequestURI();
        this.loggerStrategy.write(apiMethod, apiUrl, exception, disposer);
    }

}
