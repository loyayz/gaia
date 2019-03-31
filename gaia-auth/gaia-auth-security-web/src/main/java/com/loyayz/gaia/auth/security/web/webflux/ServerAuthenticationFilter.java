package com.loyayz.gaia.auth.security.web.webflux;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
public class ServerAuthenticationFilter extends AuthenticationWebFilter {
    private ServerAuthenticationPermissionHandler authenticationPermissionHandler;

    public ServerAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, ServerAuthenticationPermissionHandler authenticationPermissionHandler) {
        super(authenticationManager);
        super.setRequiresAuthenticationMatcher(authenticationPermissionHandler.requiresAuthenticationMatcher());
        this.authenticationPermissionHandler = authenticationPermissionHandler;
    }

    @Override
    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        return this.authenticationPermissionHandler.hasPermission(authentication, webFilterExchange.getExchange())
                .filter(hasPermission -> hasPermission)
                .switchIfEmpty(Mono.defer(() -> Mono.error(this.permissionException(authentication, webFilterExchange.getExchange()))))
                .flatMap(hasPermission -> super.onAuthenticationSuccess(authentication, webFilterExchange));
    }

    private AccessDeniedException permissionException(Authentication authentication, ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().contextPath().value();
        return new AccessDeniedException("No permission to visit " + path + " with [" + authentication + "] ");
    }

}
