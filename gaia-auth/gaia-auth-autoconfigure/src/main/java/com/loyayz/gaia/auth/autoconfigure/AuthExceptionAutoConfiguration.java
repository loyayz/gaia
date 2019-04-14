package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.security.web.AccessDeniedExceptionDefiner;
import com.loyayz.gaia.auth.security.web.AuthenticationExceptionDefiner;
import com.loyayz.gaia.auth.security.web.servlet.AuthExceptionResolver;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultAuthExceptionResolver;
import com.loyayz.gaia.auth.security.web.webflux.ServerAuthExceptionResolver;
import com.loyayz.gaia.auth.security.web.webflux.impl.DefaultServerAuthExceptionResolver;
import com.loyayz.gaia.exception.core.WebExceptionResolver;
import com.loyayz.gaia.exception.core.WebfluxExceptionResolver;
import com.loyayz.gaia.exception.core.WebmvcExceptionResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnClass({WebExceptionResolver.class, AccessDeniedExceptionDefiner.class, AuthenticationExceptionDefiner.class})
public class AuthExceptionAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnMissingBean(ServerAuthExceptionResolver.class)
    public ServerAuthExceptionResolver serverAuthExceptionResolver(WebfluxExceptionResolver resolver) {
        return new DefaultServerAuthExceptionResolver(resolver);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(AuthExceptionResolver.class)
    public AuthExceptionResolver authExceptionResolver(WebmvcExceptionResolver resolver) {
        return new DefaultAuthExceptionResolver(resolver);
    }

    @Bean(name = AccessDeniedExceptionDefiner.BEAN_NAME)
    @ConditionalOnMissingBean(AccessDeniedExceptionDefiner.class)
    public AccessDeniedExceptionDefiner accessDeniedExceptionDefiner() {
        return new AccessDeniedExceptionDefiner();
    }

    @Bean(name = AuthenticationExceptionDefiner.BEAN_NAME)
    @ConditionalOnMissingBean(AuthenticationExceptionDefiner.class)
    public AuthenticationExceptionDefiner authenticationExceptionDefiner() {
        return new AuthenticationExceptionDefiner();
    }

}
