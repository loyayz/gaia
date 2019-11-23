package com.loyayz.gaia.exception.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.loyayz.gaia.exception.ExceptionDisposer;
import com.loyayz.gaia.exception.ExceptionDisposers;
import com.loyayz.gaia.exception.ExceptionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class ExceptionWebfluxResolver implements ErrorWebExceptionHandler {
    private final ExceptionLoggerStrategy loggerStrategy;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
        return Mono.just(ExceptionDisposers.resolveByException(exception))
                .doOnNext(disposer -> this.writeLog(exchange, exception, disposer))
                .map(disposer -> disposer.getResult(exception))
                .flatMap(result -> this.writeResponse(exchange, result));
    }

    private Mono<? extends Void> writeResponse(ServerWebExchange exchange, ExceptionResult result) {
        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.valueOf(result.getStatus()));
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            byte[] responseBody = JSON.toJSONBytes(result);
            DataBuffer buffer = response.bufferFactory().wrap(responseBody);
            return response.writeWith(Mono.just(buffer))
                    .doOnError(error -> DataBufferUtils.release(buffer));
        });
    }

    private void writeLog(ServerWebExchange exchange, Throwable exception, ExceptionDisposer disposer) {
        ServerHttpRequest request = exchange.getRequest();
        String apiMethod = request.getMethodValue();
        String apiUrl = request.getPath().value();
        this.loggerStrategy.write(apiMethod, apiUrl, exception, disposer);
    }

}
