package com.loyayz.gaia.auth.security.web.servlet;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.savedrequest.NullRequestCache;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractWebSecurityAdapter extends WebSecurityConfigurerAdapter {

    protected abstract AuthenticationFilter authFilter();

    protected abstract AuthExceptionResolver exceptionResolver();

    /**
     * 其他配置
     */
    protected void additional(HttpSecurity security) {
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        this.disableAutomatic(security);
        this.cors(security);
        this.header(security);
        this.sessionPolicy(security);
        this.authPath(security);
        this.authUserFilter(security);
        this.exceptionHandling(security);
        this.additional(security);
    }

    /**
     * 禁用自动配置
     */
    protected void disableAutomatic(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    protected void cors(HttpSecurity security) throws Exception {
        security.cors();
    }

    protected void header(HttpSecurity security) throws Exception {
        security.headers().frameOptions().disable();
    }

    /**
     * stateless session
     */
    protected void sessionPolicy(HttpSecurity security) throws Exception {
        security.requestCache().requestCache(new NullRequestCache());
        security.sessionManagement().sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
    }

    protected void authPath(HttpSecurity security) throws Exception {
        security.authorizeRequests().anyRequest().permitAll();
    }

    protected void authUserFilter(HttpSecurity security) {
        AuthenticationFilter filter = this.authFilter();
        security.addFilterBefore(filter, AnonymousAuthenticationFilter.class);
    }

    protected void exceptionHandling(HttpSecurity security) throws Exception {
        security.exceptionHandling()
                .authenticationEntryPoint(this.exceptionResolver()::resolve)
                .accessDeniedHandler(this.exceptionResolver()::resolve);
    }

}
