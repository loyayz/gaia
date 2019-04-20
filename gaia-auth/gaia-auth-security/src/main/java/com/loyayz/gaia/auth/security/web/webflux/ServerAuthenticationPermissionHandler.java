package com.loyayz.gaia.auth.security.web.webflux;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ServerAuthenticationPermissionHandler {

    /**
     * 需要鉴权的校验器
     */
    ServerWebExchangeMatcher requiresAuthenticationMatcher();

    /**
     * 是否需要鉴权
     */
    default Mono<MatchResult> requiresAuthentication(ServerWebExchange exchange) {
        return this.requiresAuthenticationMatcher().matches(exchange);
    }

    /**
     * 鉴权
     */
    Mono<Boolean> hasPermission(Authentication authentication, ServerWebExchange exchange);

}
