package com.loyayz.gaia.auth.security.web.webflux.impl;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.security.AuthenticationCredentialsToken;
import com.loyayz.gaia.auth.security.web.webflux.ServerAuthCredentialsExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class DefaultServerAuthenticationConverter implements ServerAuthenticationConverter {
    private final ServerAuthCredentialsExtractor authCredentialsExtractor;

    public DefaultServerAuthenticationConverter(AuthCredentialsConfiguration credentialsConfiguration) {
        this.authCredentialsExtractor = new ServerAuthCredentialsExtractor(credentialsConfiguration);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        AuthCredentials credentials = this.authCredentialsExtractor.extract(exchange);
        Authentication token = new AuthenticationCredentialsToken(credentials);
        return Mono.just(token);
    }

}
