package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserExtractor;
import com.loyayz.gaia.auth.security.DefaultAuthenticationProvider;
import com.loyayz.gaia.auth.security.web.webflux.AbstractServerSecurityAdapter;
import com.loyayz.gaia.auth.security.web.webflux.HttpStatusServerAuthFailureHandler;
import com.loyayz.gaia.auth.security.web.webflux.ServerAuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.webflux.impl.DefaultServerAuthenticationConverter;
import com.loyayz.gaia.auth.security.web.webflux.impl.DefaultServerAuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.webflux.impl.DefaultServerSecurityAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@AutoConfigureBefore({ReactiveSecurityAutoConfiguration.class})
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class AuthWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationPermissionHandler.class)
    public ServerAuthenticationPermissionHandler serverAuthenticationPermissionHandler(AuthResourceService resourceService) {
        return new DefaultServerAuthenticationPermissionHandler(resourceService);
    }

    @Bean
    @ConditionalOnMissingBean(ReactiveAuthenticationManager.class)
    public ReactiveAuthenticationManager reactiveAuthenticationManager(AuthUserExtractor userExtractor) {
        AuthenticationProvider provider = new DefaultAuthenticationProvider(userExtractor);
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(provider);
        AuthenticationManager authenticationManager = new ProviderManager(providers);
        return new ReactiveAuthenticationManagerAdapter(authenticationManager);
    }

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationConverter.class)
    public ServerAuthenticationConverter serverAuthenticationConverter(AuthCredentialsConfiguration credentialsConfiguration) {
        return new DefaultServerAuthenticationConverter(credentialsConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean({AuthenticationWebFilter.class})
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager manager,
                                                           ServerAuthenticationPermissionHandler permissionHandler,
                                                           ServerAuthenticationConverter converter) {
        ServerAuthenticationFailureHandler failureHandler = new HttpStatusServerAuthFailureHandler(HttpStatus.UNAUTHORIZED);

        AuthenticationWebFilter filter = new AuthenticationWebFilter(manager);
        filter.setRequiresAuthenticationMatcher(permissionHandler.requiresAuthenticationMatcher());
        filter.setServerAuthenticationConverter(converter);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean(AbstractServerSecurityAdapter.class)
    public AbstractServerSecurityAdapter serverSecurityAdapter(AuthenticationWebFilter authenticationFilter,
                                                               ServerAuthenticationPermissionHandler permissionHandler) {
        return new DefaultServerSecurityAdapter(authenticationFilter, permissionHandler);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityWebFilterChain.class)
    public SecurityWebFilterChain securityFilterChain(AbstractServerSecurityAdapter securityAdapter, ServerHttpSecurity http) {
        return securityAdapter.securityFilterChain(http);
    }

}
