package com.loyayz.gaia.auth.security.web.webflux.impl;

import com.loyayz.gaia.auth.security.web.webflux.AbstractServerSecurityAdapter;
import com.loyayz.gaia.auth.security.web.webflux.ServerAuthExceptionResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@RequiredArgsConstructor
public class DefaultServerSecurityAdapter extends AbstractServerSecurityAdapter {
    private final AuthenticationWebFilter authenticationFilter;
    private final ServerAuthExceptionResolver exceptionResolver;
    @Setter
    private ReactiveAuthorizationManager<AuthorizationContext> accessDecisionManager;
    @Setter
    private CorsConfigurationSource corsConfigurationSource;

    @Override
    protected AuthenticationWebFilter authFilter() {
        return this.authenticationFilter;
    }

    @Override
    protected ServerAuthExceptionResolver exceptionResolver() {
        return this.exceptionResolver;
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
        super.authPath(security);
        ReactiveAuthorizationManager<AuthorizationContext> manager = this.getAccessDecisionManager();
        if (manager != null) {
            security.authorizeExchange().anyExchange().access(manager);
        }
    }

}
