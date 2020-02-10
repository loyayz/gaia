package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import com.loyayz.gaia.auth.security.DefaultAuthenticationManager;
import com.loyayz.gaia.auth.security.web.servlet.AbstractWebSecurityAdapter;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.servlet.HttpStatusAuthFailureHandler;
import com.loyayz.gaia.auth.security.web.servlet.NoopAuthenticationSuccessHandler;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultAuthenticationConverter;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultAuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultWebSecurityAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureBefore({SecurityAutoConfiguration.class})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@Slf4j
public class AuthWebServletAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthenticationPermissionHandler.class)
    public AuthenticationPermissionHandler authenticationPermissionHandler(AuthResourceService resourceService) {
        return new DefaultAuthenticationPermissionHandler(resourceService);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManagerResolver.class)
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(AuthUserCache userCache) {
        AuthenticationManager manager = new DefaultAuthenticationManager(userCache);
        return context -> manager;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationConverter.class)
    public AuthenticationConverter authenticationConverter(AuthCredentialsProperties credentialsProperties) {
        return new DefaultAuthenticationConverter(credentialsProperties);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFilter.class)
    public AuthenticationFilter authenticationFilter(AuthenticationPermissionHandler permissionHandler,
                                                     AuthenticationManagerResolver<HttpServletRequest> managerResolver,
                                                     AuthenticationConverter converter) {
        AuthenticationSuccessHandler successHandler = new NoopAuthenticationSuccessHandler();
        AuthenticationFailureHandler failureHandler = new HttpStatusAuthFailureHandler(HttpStatus.UNAUTHORIZED);

        AuthenticationFilter filter = new AuthenticationFilter(managerResolver, converter);
        filter.setRequestMatcher(permissionHandler.requiresAuthenticationMatcher());
        filter.setSuccessHandler(successHandler);
        filter.setFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class, AbstractWebSecurityAdapter.class})
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(AuthenticationFilter authenticationFilter,
                                                                     AuthenticationPermissionHandler permissionHandler) {
        return new DefaultWebSecurityAdapter(authenticationFilter, permissionHandler);
    }

    @Bean
    public FilterRegistrationBean disableAutoRegistration(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean<>(authenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

}
