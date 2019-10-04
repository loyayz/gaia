package com.loyayz.gaia.auth.security.web.webflux;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class ServerAuthenticationPermissionAccess implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final ServerAuthenticationPermissionHandler permissionHandler;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        return authentication
                .map(auth -> this.permissionHandler.hasPermission(auth, object.getExchange()))
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
