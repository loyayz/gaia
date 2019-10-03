package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.security.web.servlet.AbstractWebSecurityAdapter;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationPermissionAccessVoter;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationPermissionHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

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
        this.accessDecisionManager = new AuthenticationPermissionAccessVoter(permissionHandler).defaultManager();
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

}
