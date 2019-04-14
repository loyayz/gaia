package com.loyayz.gaia.auth.security.web.webflux;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ServerAuthExceptionResolver {

    Mono<Void> resolve(ServerWebExchange exchange, Throwable exception);

}
