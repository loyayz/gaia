package com.loyayz.gaia.auth.security.web.webflux;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
public class DefaultServerSecurityAdapter extends AbstractServerSecurityAdapter {
    private final AuthenticationWebFilter authenticationFilter;
    private final ServerAuthenticationPermissionHandler permissionHandler;
    @Setter
    private ReactiveAuthorizationManager<AuthorizationContext> accessDecisionManager;
    @Setter
    private CorsConfigurationSource corsConfigurationSource;

    public DefaultServerSecurityAdapter(AuthenticationWebFilter authenticationFilter,
                                        ServerAuthenticationPermissionHandler permissionHandler) {
        this.authenticationFilter = authenticationFilter;
        this.permissionHandler = permissionHandler;
        this.accessDecisionManager = new ServerAuthenticationPermissionAccess(permissionHandler);
    }

    @Override
    protected AuthenticationWebFilter authFilter() {
        return this.authenticationFilter;
    }

    @Override
    protected void cors(ServerHttpSecurity security) {
        if (this.corsConfigurationSource == null) {
            super.cors(security);
            return;
        }
        security.cors().configurationSource(this.corsConfigurationSource);
    }

    @Override
    protected void authPath(ServerHttpSecurity security) {
        ReactiveAuthorizationManager<AuthorizationContext> manager = this.getAccessDecisionManager();
        if (manager != null) {
            security.authorizeExchange().anyExchange().access(manager);
        } else {
            super.authPath(security);
        }
    }

    @RequiredArgsConstructor
    protected static class ServerAuthenticationPermissionAccess implements ReactiveAuthorizationManager<AuthorizationContext> {
        private final ServerAuthenticationPermissionHandler permissionHandler;

        @Override
        public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
            return authentication
                    .map(auth -> this.permissionHandler.hasPermission(auth, object.getExchange()))
                    .map(AuthorizationDecision::new)
                    .defaultIfEmpty(new AuthorizationDecision(false));
        }

    }

}
