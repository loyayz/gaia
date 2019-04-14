package com.loyayz.gaia.auth.security.web.webflux.impl;

import com.loyayz.gaia.auth.security.web.webflux.ServerAuthExceptionResolver;
import com.loyayz.gaia.exception.core.WebfluxExceptionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultServerAuthExceptionResolver implements ServerAuthExceptionResolver {
    private final WebfluxExceptionResolver resolver;

    @Override
    public Mono<Void> resolve(ServerWebExchange exchange, Throwable exception) {
        return resolver.handlerException(exchange, exception);
    }

}
