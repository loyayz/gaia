package com.loyayz.gaia.auth.security.web.servlet;

import com.loyayz.gaia.auth.core.AuthCorsProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
public class DefaultWebSecurityAdapter extends AbstractWebSecurityAdapter {
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationPermissionHandler permissionHandler;
    private AccessDecisionManager accessDecisionManager;
    private CorsConfigurationSource corsConfigurationSource;

    public DefaultWebSecurityAdapter(AuthenticationFilter authenticationFilter,
                                     AuthenticationPermissionHandler permissionHandler,
                                     AuthCorsProperties corsProperties) {
        this.authenticationFilter = authenticationFilter;
        this.permissionHandler = permissionHandler;
        this.accessDecisionManager = new DefaultAccessDecisionVoter(permissionHandler).accessDecisionManager();
        this.corsConfigurationSource = new DefaultCorsConfigurationSource(corsProperties).corsConfigurationSource();
    }

    @Override
    protected AuthenticationFilter authFilter() {
        return this.authenticationFilter;
    }

    @Override
    protected void cors(HttpSecurity security) throws Exception {
        if (this.corsConfigurationSource == null) {
            super.cors(security);
        } else {
            security.cors().configurationSource(this.corsConfigurationSource);
        }
    }

    @Override
    protected void authPath(HttpSecurity security) throws Exception {
        AccessDecisionManager manager = this.getAccessDecisionManager();
        if (manager != null) {
            security.authorizeRequests()
                    .anyRequest().authenticated()
                    .accessDecisionManager(manager);
        } else {
            super.authPath(security);
        }
    }

    @RequiredArgsConstructor
    protected static class DefaultAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {
        private final AuthenticationPermissionHandler permissionHandler;

        @Override
        public boolean supports(ConfigAttribute attribute) {
            return true;
        }

        @Override
        public boolean supports(Class<?> clazz) {
            return FilterInvocation.class.isAssignableFrom(clazz);
        }

        @Override
        public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
            return this.permissionHandler.hasPermission(authentication, fi.getRequest()) ? ACCESS_GRANTED : ACCESS_DENIED;
        }

        public AccessDecisionManager accessDecisionManager() {
            return new AffirmativeBased(Collections.singletonList(this));
        }

    }

    @RequiredArgsConstructor
    protected static class DefaultCorsConfigurationSource {
        private final AuthCorsProperties corsProperties;

        public CorsConfigurationSource corsConfigurationSource() {
            Map<String, AuthCorsProperties.CorsConfig> corsConfigs = this.corsProperties.getCorsConfigs();
            if (corsConfigs.isEmpty()) {
                return null;
            }
            UrlBasedCorsConfigurationSource result = new UrlBasedCorsConfigurationSource();
            for (Map.Entry<String, AuthCorsProperties.CorsConfig> entry : corsConfigs.entrySet()) {
                result.registerCorsConfiguration(entry.getKey(), entry.getValue().toCorsConfiguration());
            }
            return result;
        }
    }

}
