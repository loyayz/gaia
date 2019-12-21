package com.loyayz.gaia.exception.helper;

import com.loyayz.gaia.exception.ExceptionDisposer;
import com.loyayz.gaia.exception.ExceptionDisposers;
import com.loyayz.gaia.exception.ExceptionResult;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class ExceptionWebmvcResolver {
    @Setter
    private ExceptionLogger exceptionLogger = new DefaultExceptionLogger();

    public ExceptionResult resolve(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        ExceptionDisposer disposer = ExceptionDisposers.resolveByException(exception);
        ExceptionResult result = disposer.getResult(exception);
        response.setStatus(result.getStatus());
        this.writeLog(request, exception, disposer);
        return result;
    }

    private void writeLog(HttpServletRequest request, Throwable exception, ExceptionDisposer disposer) {
        ExceptionLoggerParam param = new ExceptionLoggerParam();
        param.setException(exception);
        param.setRequestMethod(request.getMethod());
        param.setRequestUrl(request.getRequestURI());
        param.setLogLevel(disposer.logLevel(exception));
        param.setResult(disposer.getResult(exception));
        this.exceptionLogger.write(param);
    }

}
