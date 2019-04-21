package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.security.DefaultAuthenticationProvider;
import com.loyayz.gaia.auth.core.user.AuthUserExtractor;
import com.loyayz.gaia.auth.security.web.servlet.*;
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
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager(AuthUserExtractor userExtractor) {
        AuthenticationProvider provider = new DefaultAuthenticationProvider(userExtractor);
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(provider);
        return new ProviderManager(providers);
    }

    @Bean
    @ConditionalOnMissingBean(AuthCredentialsExtractor.class)
    public AuthCredentialsExtractor<HttpServletRequest> authCredentialsExtractor(AuthCredentialsConfiguration credentialsConfiguration) {
        return new ServletAuthCredentialsExtractor(credentialsConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationConverter.class)
    public AuthenticationConverter authenticationConverter(AuthCredentialsExtractor<HttpServletRequest> extractor) {
        return new DefaultAuthenticationConverter(extractor);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (log.isDebugEnabled()) {
                log.debug("authentication success: {}", authentication.toString());
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler(AuthExceptionResolver exceptionResolver) {
        return exceptionResolver::resolve;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFilter.class)
    public AuthenticationFilter authenticationFilter(AuthenticationManager manager,
                                                     AuthenticationPermissionHandler permissionHandler,
                                                     AuthenticationConverter converter,
                                                     AuthenticationSuccessHandler successHandler,
                                                     AuthenticationFailureHandler failureHandler) {
        AuthenticationFilter filter = new AuthenticationFilter(manager);
        filter.setRequestMatcher(permissionHandler.requiresAuthenticationMatcher());
        filter.setAuthenticationConverter(converter);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationPermissionHandler.class)
    public AuthenticationPermissionHandler authenticationPermissionHandler(AuthResourceService resourceService) {
        return new DefaultAuthenticationPermissionHandler(resourceService);
    }

    @Bean
    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class, AbstractWebSecurityAdapter.class})
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(AuthenticationFilter authenticationFilter,
                                                                     AuthExceptionResolver exceptionResolver,
                                                                     AuthenticationPermissionHandler permissionHandler) {
        AccessDecisionManager accessDecisionManager = new AuthenticationPermissionAccessVoter(permissionHandler).defaultManager();

        DefaultWebSecurityAdapter result = new DefaultWebSecurityAdapter(authenticationFilter, exceptionResolver);
        result.setAccessDecisionManager(accessDecisionManager);
        return result;
    }

    @Bean
    public FilterRegistrationBean disableAutoRegistration(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean<>(authenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

}
