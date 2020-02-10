package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.credentials.AbstractAuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import com.loyayz.gaia.auth.security.SecurityToken;
import com.loyayz.gaia.auth.security.DefaultAuthenticationManager;
import com.loyayz.gaia.auth.security.web.webflux.AbstractServerSecurityAdapter;
import com.loyayz.gaia.auth.security.web.webflux.HttpStatusServerAuthFailureHandler;
import com.loyayz.gaia.auth.security.web.webflux.ServerAuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.webflux.DefaultServerAuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.webflux.DefaultServerSecurityAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
public class GaiaAuthWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationPermissionHandler.class)
    public ServerAuthenticationPermissionHandler serverAuthenticationPermissionHandler(AuthResourceService resourceService) {
        return new DefaultServerAuthenticationPermissionHandler(resourceService);
    }

    @Bean
    @ConditionalOnMissingBean(ReactiveAuthenticationManagerResolver.class)
    public ReactiveAuthenticationManagerResolver<ServerHttpRequest> reactiveAuthenticationManager(AuthUserCache userCache) {
        AuthenticationManager manager = new DefaultAuthenticationManager(userCache);
        ReactiveAuthenticationManagerAdapter authenticationManager = new ReactiveAuthenticationManagerAdapter(manager);
        return context -> Mono.just(authenticationManager);
    }

    @Bean
    @ConditionalOnMissingBean(ServerAuthenticationConverter.class)
    public ServerAuthenticationConverter serverAuthenticationConverter(AuthCredentialsProperties credentialsProperties) {
        AuthCredentialsExtractor<ServerWebExchange> extractor =
                new AbstractAuthCredentialsExtractor<ServerWebExchange>(credentialsProperties) {
                    @Override
                    protected String getHeaderToken(ServerWebExchange exchange, String headerName) {
                        return exchange.getRequest().getHeaders().getFirst(headerName);
                    }

                    @Override
                    protected String getParamToken(ServerWebExchange exchange, String paramName) {
                        return exchange.getRequest().getQueryParams().getFirst(paramName);
                    }
                };

        return exchange ->
                Mono.just(extractor.extract(exchange))
                        .map(SecurityToken::new);
    }

    @Bean
    @ConditionalOnMissingBean({AuthenticationWebFilter.class})
    public AuthenticationWebFilter authenticationWebFilter(ServerAuthenticationPermissionHandler permissionHandler,
                                                           ReactiveAuthenticationManagerResolver<ServerHttpRequest> managerResolver,
                                                           ServerAuthenticationConverter converter) {
        ServerAuthenticationFailureHandler failureHandler = new HttpStatusServerAuthFailureHandler(HttpStatus.UNAUTHORIZED);

        AuthenticationWebFilter filter = new AuthenticationWebFilter(managerResolver);
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
