package com.loyayz.gaia.auth.security.web.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper = new ObjectMapper();

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
        byte[] result;
        try {
            Map<String, Object> param = new LinkedHashMap<>(8);
            param.put("status", this.httpStatus.value());
            param.put("code", this.httpStatus.value());
            param.put("message", exception.getMessage());
            param.put("path", exchange.getRequest().getPath().value());
            result = objectMapper.writeValueAsBytes(param);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
