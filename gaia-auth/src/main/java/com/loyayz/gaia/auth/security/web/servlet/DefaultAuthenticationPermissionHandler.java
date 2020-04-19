package com.loyayz.gaia.auth.security.web.servlet;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.loyayz.gaia.auth.core.authorization.AuthResource;
import com.loyayz.gaia.auth.core.authorization.AuthResourcePermission;
import com.loyayz.gaia.auth.core.authorization.AuthResourceRefreshedListener;
import com.loyayz.gaia.auth.core.authorization.AuthResourceService;
import com.loyayz.gaia.auth.security.SecurityToken;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultAuthenticationPermissionHandler implements AuthenticationPermissionHandler, AuthResourceRefreshedListener {
    private final AuthResourceService resourceService;
    private DelegatingRequestMatcher protectMatcher = new DelegatingRequestMatcher(AnyRequestMatcher.INSTANCE);
    /**
     * 资源对应的权限配置
     */
    private Map<RequestMatcher, AuthResourcePermission> protectMatcherResourcePermissions;
    /**
     * 角色只能访问的资源
     * <roleCode,matcher>
     */
    private Map<String, RequestMatcher> roleMatchers;

    public DefaultAuthenticationPermissionHandler(AuthResourceService resourceService) {
        this.resourceService = resourceService;
        this.init();
    }

    @Override
    public RequestMatcher requiresAuthenticationMatcher() {
        return this.protectMatcher;
    }

    @Override
    public Boolean hasPermission(Authentication authentication, HttpServletRequest request) {
        if (!this.requiresAuthentication(request)) {
            return true;
        }
        if (SecurityToken.class.isAssignableFrom(authentication.getClass())) {
            return this.hasPermission((SecurityToken) authentication, request);
        }
        return false;
    }

    private boolean hasPermission(SecurityToken token, HttpServletRequest request) {
        boolean hasResourcePermission = this.hasResourcePermission(token, request);
        if (hasResourcePermission) {
            return this.hasRolePermission(token, request);
        }
        return false;
    }

    private boolean hasResourcePermission(SecurityToken token, HttpServletRequest request) {
        boolean reject = this.protectMatcherResourcePermissions
                .entrySet()
                .parallelStream()
                // 无权限访问受保护资源
                .anyMatch(protectMatcher -> {
                    boolean hasPermission = true;
                    boolean matchPath = protectMatcher.getKey().matches(request);
                    if (matchPath) {
                        AuthResourcePermission permission = protectMatcher.getValue();
                        hasPermission = token.hasAnyRole(permission.getAllowedRoles());
                    }
                    return !hasPermission;
                });
        return !reject;
    }

    private boolean hasRolePermission(SecurityToken token, HttpServletRequest request) {
        List<String> roleCodes = token.getRoleCodes();
        if (roleCodes.isEmpty()) {
            return true;
        }
        List<RequestMatcher> userMatchers = Lists.newArrayList();
        for (String userRole : roleCodes) {
            if (this.roleMatchers.containsKey(userRole)) {
                userMatchers.add(this.roleMatchers.get(userRole));
            } else {
                // 用户中有无限制资源访问的角色
                return true;
            }
        }
        return new OrRequestMatcher(userMatchers).matches(request);
    }

    private void init() {
        this.setProtectMatcher();
        this.setProtectMatcherResourcePermissions();
        this.setProtectMatcherRolePermissions();
    }

    private void setProtectMatcher() {
        List<AuthResource> publicResources = this.resourceService.listPermitResources();
        this.resourceToMatcher(publicResources).ifPresent(matcher ->
                this.protectMatcher.setMatcher(new NegatedRequestMatcher(matcher))
        );
    }

    private void setProtectMatcherResourcePermissions() {
        Map<RequestMatcher, AuthResourcePermission> permissions = Maps.newHashMap();
        this.resourceService.listResourcePermissions().forEach(permission ->
                this.resourceToMatcher(permission).ifPresent(matcher ->
                        permissions.put(matcher, permission)
                )
        );
        this.protectMatcherResourcePermissions = permissions;
    }

    private void setProtectMatcherRolePermissions() {
        Multimap<String, RequestMatcher> roleMatcherMap = HashMultimap.create();
        this.resourceService.listRolePermissions().forEach(rolePermission -> {
            if (rolePermission.valid()) {
                String role = rolePermission.getRole();
                List<AuthResource> resources = rolePermission.getResources();
                this.resourceToMatcher(resources).ifPresent(matcher -> roleMatcherMap.put(role, matcher));
            }
        });
        this.roleMatchers = roleMatcherMap.asMap().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new OrRequestMatcher(Lists.newArrayList(e.getValue()))
                ));
    }

    private Optional<RequestMatcher> resourceToMatcher(List<AuthResource> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return Optional.empty();
        }
        List<RequestMatcher> matchers = resources.stream()
                .map(this::resourceToMatcher)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return matchers.isEmpty() ? Optional.empty() : Optional.of(new OrRequestMatcher(matchers));
    }

    private Optional<RequestMatcher> resourceToMatcher(AuthResource resource) {
        String path = resource.getPath();
        if (!StringUtils.hasText(path)) {
            return Optional.empty();
        }
        List<RequestMatcher> matchers = resource.getMethods()
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
        return matchers.isEmpty() ? Optional.empty() : Optional.of(new OrRequestMatcher(matchers));
    }

    @Override
    public void onResourceRefreshed() {
        this.init();
    }

    private static class DelegatingRequestMatcher implements RequestMatcher {
        @Setter
        private RequestMatcher matcher;

        DelegatingRequestMatcher(RequestMatcher matcher) {
            this.matcher = matcher;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return this.matcher.matches(request);
        }

    }

}
