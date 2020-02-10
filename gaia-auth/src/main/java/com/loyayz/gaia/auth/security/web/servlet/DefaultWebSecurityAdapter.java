package com.loyayz.gaia.auth.security.web.servlet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
public class DefaultWebSecurityAdapter extends AbstractWebSecurityAdapter {
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationPermissionHandler permissionHandler;
    @Setter
    private AccessDecisionManager accessDecisionManager;
    @Setter
    private CorsConfigurationSource corsConfigurationSource;

    public DefaultWebSecurityAdapter(AuthenticationFilter authenticationFilter,
                                     AuthenticationPermissionHandler permissionHandler) {
        this.authenticationFilter = authenticationFilter;
        this.permissionHandler = permissionHandler;
        this.accessDecisionManager = new DefaultAccessDecisionVoter(permissionHandler).accessDecisionManager();
    }

    @Override
    protected AuthenticationFilter authFilter() {
        return this.authenticationFilter;
    }

    @Override
    protected void cors(HttpSecurity security) throws Exception {
        super.cors(security);
        if (this.corsConfigurationSource != null) {
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

}
