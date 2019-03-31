package com.loyayz.gaia.auth.security.web.webflux.impl;

import com.loyayz.gaia.auth.security.web.webflux.ServerAuthenticationExceptionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultServerAuthenticationExceptionResolver implements ServerAuthenticationExceptionResolver {

    @Override
    public Mono<Void> resolve(ServerWebExchange exchange, Throwable exception) {
        return Mono.defer(() -> {
            log.error("authentication fail.", exception);

            HttpStatus status = HttpStatus.UNAUTHORIZED;
            if (exception instanceof AccessDeniedException) {
                status = HttpStatus.FORBIDDEN;
            }
            exchange.getResponse().setStatusCode(status);
            return Mono.empty();
        });
    }

}
