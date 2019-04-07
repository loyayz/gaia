package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserExtractor;
import com.loyayz.gaia.auth.security.DefaultAuthenticationProvider;
import com.loyayz.gaia.auth.security.web.servlet.*;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultAuthenticationConverter;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultAuthenticationExceptionResolver;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultAuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.servlet.impl.DefaultWebSecurityAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@ConditionalOnClass(AbstractWebSecurityAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureBefore({SecurityAutoConfiguration.class})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@Slf4j
public class AuthWebServletAutoConfiguration {
    private final AuthCredentialsConfiguration securityCredentialsConfiguration;
    private final AuthUserExtractor securityUserExtractor;
    private final AuthResourceService securityResourceService;

    @Bean
    @ConditionalOnMissingBean(AuthenticationExceptionResolver.class)
    public AuthenticationExceptionResolver authenticationExceptionResolver() {
        return new DefaultAuthenticationExceptionResolver();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager() {
        AuthenticationProvider provider = new DefaultAuthenticationProvider(this.securityUserExtractor);
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(provider);
        return new ProviderManager(providers);
    }

    @Bean
    @ConditionalOnMissingBean(AuthCredentialsExtractor.class)
    public AuthCredentialsExtractor<HttpServletRequest> authCredentialsExtractor() {
        return new ServletAuthCredentialsExtractor(this.securityCredentialsConfiguration);
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
    public AuthenticationFailureHandler authenticationFailureHandler(AuthenticationExceptionResolver exceptionResolver) {
        return exceptionResolver::resolve;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFilter.class)
    public AuthenticationFilter authenticationFilter(AuthenticationManager manager,
                                                     AuthenticationPermissionHandler permissionHandler,
                                                     AuthenticationConverter converter,
                                                     AuthenticationSuccessHandler successHandler,
                                                     AuthenticationFailureHandler failureHandler) {
        AuthenticationFilter filter = new AuthenticationFilter(manager, permissionHandler);
        filter.setAuthenticationConverter(converter);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationPermissionHandler.class)
    public AuthenticationPermissionHandler authenticationPermissionHandler() {
        return new DefaultAuthenticationPermissionHandler(this.securityResourceService);
    }

    @Bean
    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class, AbstractWebSecurityAdapter.class})
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(AuthenticationFilter authenticationFilter,
                                                                     AuthenticationExceptionResolver exceptionResolver) {
        return new DefaultWebSecurityAdapter(authenticationFilter, exceptionResolver);
    }

    @Bean
    public FilterRegistrationBean disableAutoRegistration(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean<>(authenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

}
