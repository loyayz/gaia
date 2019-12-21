package com.loyayz.gaia.exception.helper;

import com.loyayz.gaia.exception.ExceptionDisposer;
import com.loyayz.gaia.exception.ExceptionDisposers;
import com.loyayz.gaia.exception.ExceptionResult;
import lombok.Setter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class ExceptionWebfluxResolver {
    @Setter
    private ExceptionLogger exceptionLogger = new DefaultExceptionLogger();

    public Mono<ExceptionResult> resolve(ServerWebExchange exchange, Throwable exception) {
        return Mono.just(ExceptionDisposers.resolveByException(exception))
                .doOnNext(disposer -> this.writeLog(exchange, exception, disposer))
                .map(disposer -> disposer.getResult(exception));
    }

    private void writeLog(ServerWebExchange exchange, Throwable exception, ExceptionDisposer disposer) {
        ExceptionLoggerParam param = new ExceptionLoggerParam();
        ServerHttpRequest request = exchange.getRequest();
        param.setException(exception);
        param.setRequestMethod(request.getMethodValue());
        param.setRequestUrl(request.getPath().value());
        param.setLogLevel(disposer.logLevel(exception));
        param.setResult(disposer.getResult(exception));
        this.exceptionLogger.write(param);
    }

}
