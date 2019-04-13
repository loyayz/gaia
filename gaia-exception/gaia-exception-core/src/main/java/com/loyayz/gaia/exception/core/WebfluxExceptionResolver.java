package com.loyayz.gaia.exception.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
@RequiredArgsConstructor
public class WebfluxExceptionResolver implements WebExceptionResolver {
    private final ExceptionResolver resolver;

    public Mono<Void> handlerException(ServerWebExchange exchange, Throwable exception) {
        return Mono.just(this.resolver.getExceptionResult(exception))
                .doOnNext(result -> this.writeLog(exchange, exception, result))
                .flatMap(result -> this.writeResponse(exchange, result));
    }

    private Mono<? extends Void> writeResponse(ServerWebExchange exchange, ExceptionResult result) {
        return ServerResponse.status(this.getHttpStatus(result))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(result))
                .flatMap(serverResponse -> {
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                    return serverResponse.writeTo(exchange, new DefaultResponseContext());
                });
    }

    private HttpStatus getHttpStatus(ExceptionResult result) {
        Integer status = result.getStatus();
        return HttpStatus.valueOf(status);
    }

    private void writeLog(ServerWebExchange exchange, Throwable exception, ExceptionResult result) {
        if (log.isWarnEnabled()) {
            log.warn(this.logMsg(exchange, result), exception);
        } else if (log.isDebugEnabled()) {
            log.debug(this.logMsg(exchange, result), exception);
        }
    }

    private String logMsg(ServerWebExchange exchange, ExceptionResult result) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().name();
        String url = request.getURI().getPath();
        return "[" + method + ": " + url + "]  " + result;
    }

    private class DefaultResponseContext implements ServerResponse.Context {

        private final HandlerStrategies strategies = HandlerStrategies.withDefaults();

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return this.strategies.messageWriters();
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return this.strategies.viewResolvers();
        }

    }

}
