package com.loyayz.gaia.auth.security.web.webflux.impl;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import com.loyayz.gaia.auth.security.AuthenticationCredentialsToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultServerAuthenticationConverter implements ServerAuthenticationConverter {
    private final AuthCredentialsExtractor<ServerWebExchange> authCredentialsExtractor;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        AuthCredentials credentials = this.authCredentialsExtractor.extract(exchange);
        Authentication token = new AuthenticationCredentialsToken(credentials);
        return Mono.just(token);
    }

}
