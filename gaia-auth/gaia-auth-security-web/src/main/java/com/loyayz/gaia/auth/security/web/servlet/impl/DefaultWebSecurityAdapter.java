package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.security.web.servlet.AbstractWebSecurityAdapter;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationExceptionResolver;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@RequiredArgsConstructor
public class DefaultWebSecurityAdapter extends AbstractWebSecurityAdapter {
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationExceptionResolver authenticationExceptionResolver;
    @Setter
    private CorsConfigurationSource corsConfigurationSource;

    @Override
    protected AuthenticationFilter authFilter() {
        return this.authenticationFilter;
    }

    @Override
    protected AuthenticationExceptionResolver exceptionResolver() {
        return this.authenticationExceptionResolver;
    }

    @Override
    protected void cors(HttpSecurity security) throws Exception {
        if (this.corsConfigurationSource == null) {
            super.cors(security);
            return;
        }
        security.cors().configurationSource(this.corsConfigurationSource);
    }

}
