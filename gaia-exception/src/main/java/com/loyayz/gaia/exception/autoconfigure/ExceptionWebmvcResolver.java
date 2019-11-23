package com.loyayz.gaia.exception.autoconfigure;

import com.loyayz.gaia.exception.ExceptionDisposer;
import com.loyayz.gaia.exception.ExceptionDisposers;
import com.loyayz.gaia.exception.ExceptionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class ExceptionWebmvcResolver {
    private final ExceptionLoggerStrategy loggerStrategy;

    @ExceptionHandler(value = Throwable.class)
    public ExceptionResult handler(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
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
