package com.loyayz.gaia.auth.security.web.servlet;

import com.google.common.collect.Lists;
import com.loyayz.gaia.auth.core.authorization.AuthPermission;
import com.loyayz.gaia.auth.core.authorization.AuthPermissionProvider;
import com.loyayz.gaia.auth.core.authorization.AuthResource;
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
public class DefaultAuthenticationPermissionHandler implements AuthenticationPermissionHandler {
    private static RequestMatcher ALL_MATCHER = AnyRequestMatcher.INSTANCE;
    private final AuthPermissionProvider permissionProvider;
    private DelegatingRequestMatcher protectMatcher = new DelegatingRequestMatcher(ALL_MATCHER);
    private Map<RequestMatcher, List<String>> matcherRoles;
    private Map<String, RequestMatcher> roleMatchers;

    public DefaultAuthenticationPermissionHandler(AuthPermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
        this.init();
    }

    @Override
    public void refresh() {
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
        boolean reject = this.matcherRoles
                .entrySet()
                .parallelStream()
                // 无权限访问受保护资源
                .anyMatch(protectMatcher -> {
                    boolean hasPermission = true;
                    boolean matchPath = protectMatcher.getKey().matches(request);
                    if (matchPath) {
                        hasPermission = token.hasAnyRole(protectMatcher.getValue());
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
        for (String roleCode : roleCodes) {
            if (this.roleMatchers.containsKey(roleCode)) {
                userMatchers.add(this.roleMatchers.get(roleCode));
            } else {
                // 拥有不用限制资源访问的角色
                return true;
            }
        }
        return new OrRequestMatcher(userMatchers).matches(request);
    }

    private void init() {
        this.setProtectMatcher();
        this.setProtectMatcherPermission();
    }

    private void setProtectMatcher() {
        List<AuthResource> publicResources = this.permissionProvider.listPermitResources();
        Optional<RequestMatcher> matcherOptional = this.resourceToMatcher(publicResources);
        if (matcherOptional.isPresent()) {
            this.protectMatcher.setMatcher(new NegatedRequestMatcher(matcherOptional.get()));
        } else {
            this.protectMatcher.setMatcher(ALL_MATCHER);
        }
    }

    private void setProtectMatcherPermission() {
        AuthPermission resourcePermission = this.permissionProvider.getPermission();

        this.matcherRoles = resourcePermission.getResourceRoles()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        p -> this.resourceToMatcher(p.getKey()).orElse(ALL_MATCHER),
                        Map.Entry::getValue
                ));
        this.roleMatchers = resourcePermission.getRoleResources()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        p -> this.resourceToMatcher(p.getValue()).orElse(ALL_MATCHER)
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
