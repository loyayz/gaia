package com.loyayz.gaia.auth.security.web.webflux;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class HttpStatusServerAuthFailureHandler
        implements ServerAuthenticationEntryPoint, ServerAuthenticationFailureHandler, ServerAccessDeniedHandler {
    private final HttpStatus httpStatus;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {
        return this.responseError(exchange, exception);
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange exchange, AuthenticationException exception) {
        return this.responseError(exchange.getExchange(), exception);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException exception) {
        return this.responseError(exchange, exception);
    }

    private Mono<Void> responseError(ServerWebExchange exchange, Exception exception) {
        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(this.httpStatus);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            byte[] responseBody = this.responseBody(exchange, exception);
            DataBuffer buffer = response.bufferFactory().wrap(responseBody);
            return response.writeWith(Mono.just(buffer))
                    .doOnError(error -> DataBufferUtils.release(buffer));
        });
    }

    private byte[] responseBody(ServerWebExchange exchange, Exception exception) {
        Map<String, Object> result = new LinkedHashMap<>(8);
        result.put("status", this.httpStatus.value());
        result.put("code", this.httpStatus.value());
        result.put("message", exception.getMessage());
        result.put("path", exchange.getRequest().getPath().value());
        return JSON.toJSONBytes(result);
    }

}
