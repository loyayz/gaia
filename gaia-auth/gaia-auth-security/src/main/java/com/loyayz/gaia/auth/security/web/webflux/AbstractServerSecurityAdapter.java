package com.loyayz.gaia.auth.security.web.webflux;

import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.web.server.WebFilter;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractServerSecurityAdapter {

    protected abstract AuthenticationWebFilter authFilter();

    protected abstract ServerAuthExceptionResolver exceptionResolver();

    /**
     * 其他配置
     */
    protected void additional(ServerHttpSecurity security) {
    }

    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity security) {
        this.disableAutomatic(security);
        this.cors(security);
        this.header(security);
        this.sessionPolicy(security);
        this.authPath(security);
        this.authUserFilter(security);
        this.exceptionHandling(security);
        this.additional(security);
        return security.build();
    }

    /**
     * 禁用自动配置
     */
    protected void disableAutomatic(ServerHttpSecurity security) {
        security
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    protected void cors(ServerHttpSecurity security) {
        security.cors();
    }

    protected void header(ServerHttpSecurity security) {
        security.headers().frameOptions().disable();
    }

    /**
     * stateless session
     */
    protected void sessionPolicy(ServerHttpSecurity security) {
        security.requestCache().requestCache(NoOpServerRequestCache.getInstance());
        security.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
    }

    protected void authPath(ServerHttpSecurity security) {
        security.authorizeExchange().anyExchange().permitAll();
    }

    protected void authUserFilter(ServerHttpSecurity security) {
        WebFilter filter = this.authFilter();
        security.addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION);
    }

    protected void exceptionHandling(ServerHttpSecurity security) {
        security.exceptionHandling()
                .authenticationEntryPoint(this.exceptionResolver()::resolve)
                .accessDeniedHandler(this.exceptionResolver()::resolve);
    }

}
