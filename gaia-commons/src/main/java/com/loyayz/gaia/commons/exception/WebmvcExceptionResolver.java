package com.loyayz.gaia.commons.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
@RequiredArgsConstructor
public class WebmvcExceptionResolver {
    private final ExceptionResolver resolver;

    public ExceptionResult handlerException(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        ExceptionResult result = this.resolver.getExceptionResult(exception);
        response.setStatus(result.getStatus());

        this.writeLog(request, exception, result);
        return result;
    }

    private void writeLog(HttpServletRequest request, Throwable exception, ExceptionResult result) {
        if (log.isWarnEnabled()) {
            log.warn(this.logMsg(request, result), exception);
        } else if (log.isDebugEnabled()) {
            log.debug(this.logMsg(request, result), exception);
        }
    }

    private String logMsg(HttpServletRequest request, ExceptionResult result) {
        String method = request.getMethod();
        String url = request.getRequestURI();
        return "[" + method + ": " + url + "]  " + result;
    }

}
