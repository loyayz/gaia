package com.loyayz.gaia.auth.security.web.webflux;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@AllArgsConstructor
public class DelegatingServerWebExchangeMatcher implements ServerWebExchangeMatcher {
    private ServerWebExchangeMatcher matcher;

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return this.matcher.matches(exchange);
    }

}
