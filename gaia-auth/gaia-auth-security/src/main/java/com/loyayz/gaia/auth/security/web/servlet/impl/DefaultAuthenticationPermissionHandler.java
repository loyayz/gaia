package com.loyayz.gaia.auth.security.web.servlet.impl;


import com.google.common.collect.Maps;
import com.loyayz.gaia.auth.core.resource.AuthResource;
import com.loyayz.gaia.auth.core.resource.AuthResourcePermission;
import com.loyayz.gaia.auth.core.resource.AuthResourceRefreshedListener;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationPermissionHandler;
import com.loyayz.gaia.auth.security.web.servlet.DelegatingRequestMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultAuthenticationPermissionHandler implements AuthenticationPermissionHandler, AuthResourceRefreshedListener {
    private final AuthResourceService resourceService;

    public DefaultAuthenticationPermissionHandler(AuthResourceService resourceService) {
        this.resourceService = resourceService;
        this.init();
    }

    private DelegatingRequestMatcher protectMatcher = new DelegatingRequestMatcher(AnyRequestMatcher.INSTANCE);
    private Map<RequestMatcher, AuthResourcePermission> protectMatcherPermission;

    @Override
    public RequestMatcher requiresAuthenticationMatcher() {
        return this.protectMatcher;
    }

    @Override
    public boolean hasPermission(Authentication authentication, HttpServletRequest request) {
        if (!this.requiresAuthentication(request)) {
            return true;
        }
        if (AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return this.validAuthentication(authentication, request);
    }

    private boolean validAuthentication(Authentication authentication, HttpServletRequest request) {
        SecurityExpressionOperations authenticationOperations = new DefaultSecurityExpressionRoot(authentication);
        boolean reject = Maps.newHashMap(this.protectMatcherPermission)
                .entrySet()
                .parallelStream()
                // 无权限访问受保护资源
                .anyMatch(protectMatcher -> {
                    boolean matchPath = protectMatcher.getKey().matches(request);
                    if (matchPath) {
                        AuthResourcePermission permission = protectMatcher.getValue();
                        if (permission.isPermit()) {
                            return false;
                        }
                        String[] roles = permission.getAllowedRoles().toArray(new String[]{});
                        return !authenticationOperations.hasAnyRole(roles);
                    } else {
                        return false;
                    }
                });
        return !reject;
    }

    private void init() {
        this.setProtectMatcher();
        this.setProtectMatcherPermission();
    }

    private void setProtectMatcher() {
        List<RequestMatcher> permitMatchers = this.resourceService.listPermitResources()
                .stream()
                .flatMap(resource -> this.resourceToMatchers(resource).stream())
                .collect(Collectors.toList());
        OrRequestMatcher publicMatchers = new OrRequestMatcher(permitMatchers);
        this.protectMatcher.setMatcher(new NegatedRequestMatcher(publicMatchers));
    }

    private void setProtectMatcherPermission() {
        Map<RequestMatcher, AuthResourcePermission> permissions = Maps.newHashMap();
        this.resourceService.listProtectResourcePermission().forEach((resource, permission) -> {
            this.resourceToMatchers(resource).forEach((matcher -> {
                permissions.put(matcher, permission);
            }));
        });
        this.protectMatcherPermission = permissions;
    }

    private List<RequestMatcher> resourceToMatchers(AuthResource resource) {
        String path = resource.getPath();
        if (!StringUtils.hasText(path)) {
            return Collections.emptyList();
        }
        return resource.getMethods()
                .stream()
                .map(method -> {
                            HttpMethod httpMethod = HttpMethod.resolve(method);
                            if (httpMethod == null) {
                                return new AntPathRequestMatcher(path);
                            } else {
                                return new AntPathRequestMatcher(path, method);
                            }
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public void onResourceRefreshed() {
        this.init();
    }

    class DefaultSecurityExpressionRoot extends SecurityExpressionRoot {
        DefaultSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }
    }

}
