package com.loyayz.gaia.auth.security.web.webflux;

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
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultServerAuthenticationPermissionHandler implements ServerAuthenticationPermissionHandler, AuthResourceRefreshedListener {
    private final AuthResourceService resourceService;
    private DelegatingServerWebExchangeMatcher protectMatcher = new DelegatingServerWebExchangeMatcher(ServerWebExchangeMatchers.anyExchange());
    private Map<ServerWebExchangeMatcher, AuthResourcePermission> protectMatcherResourcePermissions;
    private Map<String, ServerWebExchangeMatcher> roleMatchers;

    public DefaultServerAuthenticationPermissionHandler(AuthResourceService resourceService) {
        this.resourceService = resourceService;
        this.init();
    }

    @Override
    public ServerWebExchangeMatcher requiresAuthenticationMatcher() {
        return this.protectMatcher;
    }

    @Override
    public Boolean hasPermission(Authentication authentication, ServerWebExchange exchange) {
        if (!this.requiresAuthentication(exchange)) {
            return true;
        }
        if (SecurityToken.class.isAssignableFrom(authentication.getClass())) {
            return this.hasPermission((SecurityToken) authentication, exchange);
        }
        return false;
    }

    private boolean hasPermission(SecurityToken token, ServerWebExchange exchange) {
        boolean hasResourcePermission = this.hasResourcePermission(token, exchange);
        if (hasResourcePermission) {
            return this.hasRolePermission(token, exchange);
        }
        return false;
    }

    private boolean hasResourcePermission(SecurityToken token, ServerWebExchange exchange) {
        boolean reject = this.protectMatcherResourcePermissions
                .entrySet()
                .parallelStream()
                // 无权限访问受保护资源
                .anyMatch(protectMatcher -> protectMatcher.getKey().matches(exchange)
                        .map(matchPath -> {
                            boolean hasPermission = true;
                            if (matchPath.isMatch()) {
                                AuthResourcePermission permission = protectMatcher.getValue();
                                hasPermission = token.hasAnyRole(permission.getAllowedRoles());
                            }
                            return !hasPermission;
                        })
                        .block()
                );
        return !reject;
    }

    private boolean hasRolePermission(SecurityToken token, ServerWebExchange exchange) {
        List<String> roleCodes = token.getRoleCodes();
        if (roleCodes.isEmpty()) {
            return true;
        }
        List<ServerWebExchangeMatcher> userMatchers = Lists.newArrayList();
        for (String userRole : roleCodes) {
            if (this.roleMatchers.containsKey(userRole)) {
                userMatchers.add(this.roleMatchers.get(userRole));
            } else {
                // 用户中有无限制资源访问的角色
                return true;
            }
        }
        Boolean result = new OrServerWebExchangeMatcher(userMatchers).matches(exchange)
                .map(ServerWebExchangeMatcher.MatchResult::isMatch)
                .block();
        return result == null ? false : result;
    }

    private void init() {
        this.setProtectMatcher();
        this.setProtectMatcherResourcePermissions();
        this.setProtectMatcherRolePermissions();
    }

    private void setProtectMatcher() {
        List<AuthResource> publicResources = this.resourceService.listPermitResources();
        this.resourceToMatcher(publicResources).ifPresent(matcher ->
                this.protectMatcher.setMatcher(new NegatedServerWebExchangeMatcher(matcher))
        );
    }

    private void setProtectMatcherResourcePermissions() {
        Map<ServerWebExchangeMatcher, AuthResourcePermission> permissions = Maps.newHashMap();
        this.resourceService.listResourcePermissions().forEach(permission ->
                this.resourceToMatcher(permission).ifPresent(matcher ->
                        permissions.put(matcher, permission)
                )
        );
        this.protectMatcherResourcePermissions = permissions;
    }

    private void setProtectMatcherRolePermissions() {
        Multimap<String, ServerWebExchangeMatcher> roleMatcherMap = HashMultimap.create();
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
                        e -> new OrServerWebExchangeMatcher(Lists.newArrayList(e.getValue()))
                ));
    }

    private Optional<ServerWebExchangeMatcher> resourceToMatcher(List<AuthResource> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return Optional.empty();
        }
        List<ServerWebExchangeMatcher> matchers = resources.stream()
                .map(this::resourceToMatcher)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return matchers.isEmpty() ? Optional.empty() : Optional.of(new OrServerWebExchangeMatcher(matchers));
    }

    private Optional<ServerWebExchangeMatcher> resourceToMatcher(AuthResource resource) {
        String path = resource.getPath();
        if (!StringUtils.hasText(path)) {
            return Optional.empty();
        }
        List<ServerWebExchangeMatcher> matchers = resource.getMethods()
                .stream()
                .map(method -> {
                    HttpMethod httpMethod = HttpMethod.resolve(method);
                    return ServerWebExchangeMatchers.pathMatchers(httpMethod, path);
                })
                .collect(Collectors.toList());
        return matchers.isEmpty() ? Optional.empty() : Optional.of(new OrServerWebExchangeMatcher(matchers));
    }

    @Override
    public void onResourceRefreshed() {
        this.init();
    }

    private static class DelegatingServerWebExchangeMatcher implements ServerWebExchangeMatcher {
        @Setter
        private ServerWebExchangeMatcher matcher;

        DelegatingServerWebExchangeMatcher(ServerWebExchangeMatcher matcher) {
            this.matcher = matcher;
        }

        @Override
        public Mono<MatchResult> matches(ServerWebExchange exchange) {
            return this.matcher.matches(exchange);
        }

    }

}
