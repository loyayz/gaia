package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.security.DefaultAuthenticationProvider;
import com.loyayz.gaia.auth.core.user.AuthUserExtractor;
import com.loyayz.gaia.auth.security.web.webflux.*;
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
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    @ConditionalOnMissingBean(ReactiveAuthenticationManager.class)
    public ReactiveAuthenticationManager reactiveAuthenticationManager(AuthUserExtractor userExtractor) {
        AuthenticationProvider provider = new DefaultAuthenticationProvider(userExtractor);
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(provider);
        AuthenticationManager authenticationManager = new ProviderManager(providers);
        return new ReactiveAuthenticationManagerAdapter(authenticationManager);
    }

    @Bean
    @ConditionalOnMissingBean(AuthCredentialsExtractor.class)
    public AuthCredentialsExtractor<ServerWebExchange> authCredentialsExtractor(AuthCredentialsConfiguration credentialsConfiguration) {
        return new ServerAuthCredentialsExtractor(credentialsConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationConverter.class)
    public ServerAuthenticationConverter serverAuthenticationConverter(AuthCredentialsExtractor<ServerWebExchange> extractor) {
        return new DefaultServerAuthenticationConverter(extractor);
    }

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationSuccessHandler.class)
    public ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler() {
        return new WebFilterChainServerAuthenticationSuccessHandler() {
            @Override
            public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
                if (log.isDebugEnabled()) {
                    log.debug("authentication success: {}", authentication.toString());
                }
                return super.onAuthenticationSuccess(webFilterExchange, authentication);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationFailureHandler.class)
    public ServerAuthenticationFailureHandler serverAuthenticationFailureHandler(ServerAuthExceptionResolver exceptionResolver) {
        return (exchange, exception) -> exceptionResolver.resolve(exchange.getExchange(), exception);
    }

    @Bean
    @ConditionalOnMissingBean({AuthenticationWebFilter.class})
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager manager,
                                                           ServerAuthenticationPermissionHandler permissionHandler,
                                                           ServerAuthenticationConverter converter,
                                                           ServerAuthenticationSuccessHandler successHandler,
                                                           ServerAuthenticationFailureHandler failureHandler) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(manager);
        filter.setRequiresAuthenticationMatcher(permissionHandler.requiresAuthenticationMatcher());
        filter.setServerAuthenticationConverter(converter);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationPermissionHandler.class)
    public ServerAuthenticationPermissionHandler serverAuthenticationPermissionHandler(AuthResourceService resourceService) {
        return new DefaultServerAuthenticationPermissionHandler(resourceService);
    }

    @Bean
    @ConditionalOnMissingBean(AbstractServerSecurityAdapter.class)
    public AbstractServerSecurityAdapter serverSecurityAdapter(AuthenticationWebFilter authenticationFilter,
                                                               ServerAuthExceptionResolver exceptionResolver,
                                                               ServerAuthenticationPermissionHandler permissionHandler) {
        ServerAuthenticationPermissionAccess accessDecisionManager = new ServerAuthenticationPermissionAccess(permissionHandler);

        DefaultServerSecurityAdapter result = new DefaultServerSecurityAdapter(authenticationFilter, exceptionResolver);
        result.setAccessDecisionManager(accessDecisionManager);
        return result;
    }

    @Bean
    @ConditionalOnMissingBean(SecurityWebFilterChain.class)
    public SecurityWebFilterChain securityFilterChain(AbstractServerSecurityAdapter securityAdapter, ServerHttpSecurity http) {
        return securityAdapter.securityFilterChain(http);
    }

}
