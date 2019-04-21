package com.loyayz.gaia.auth.security.web.webflux;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;

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
    default Boolean requiresAuthentication(ServerWebExchange exchange) {
        return this.requiresAuthenticationMatcher().matches(exchange)
                .map(r -> r.isMatch())
                .defaultIfEmpty(Boolean.FALSE)
                .block();
    }

    /**
     * 鉴权
     */
    Boolean hasPermission(Authentication authentication, ServerWebExchange exchange);

}
