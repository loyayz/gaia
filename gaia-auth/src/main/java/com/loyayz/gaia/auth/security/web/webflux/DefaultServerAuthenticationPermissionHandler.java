package com.loyayz.gaia.auth.security.web.webflux;

import com.google.common.collect.Maps;
import com.loyayz.gaia.auth.core.resource.AuthResource;
import com.loyayz.gaia.auth.core.resource.AuthResourcePermission;
import com.loyayz.gaia.auth.core.resource.AuthResourceRefreshedListener;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultServerAuthenticationPermissionHandler implements ServerAuthenticationPermissionHandler, AuthResourceRefreshedListener {
    private final AuthResourceService resourceService;
    private DelegatingServerWebExchangeMatcher protectMatcher = new DelegatingServerWebExchangeMatcher(ServerWebExchangeMatchers.anyExchange());
    private Map<ServerWebExchangeMatcher, AuthResourcePermission> protectMatcherPermission;

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
            return Boolean.TRUE;
        }
        if (AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return Boolean.FALSE;
        }
        return this.validAuthentication(authentication, exchange);
    }

    private boolean validAuthentication(Authentication authentication, ServerWebExchange exchange) {
        SecurityExpressionOperations authenticationOperations = new DefaultSecurityExpressionRoot(authentication);
        boolean reject = Maps.newHashMap(this.protectMatcherPermission)
                .entrySet()
                .parallelStream()
                // 无权限访问受保护资源
                .anyMatch(protectMatcher -> protectMatcher.getKey().matches(exchange)
                        .map(match -> {
                            if (match.isMatch()) {
                                AuthResourcePermission permission = protectMatcher.getValue();
                                if (permission.isPermit()) {
                                    return false;
                                }
                                String[] roles = permission.getAllowedRoles().toArray(new String[]{});
                                return !authenticationOperations.hasAnyRole(roles);
                            } else {
                                return false;
                            }
                        })
                        .block()
                );
        return !reject;
    }

    private void init() {
        this.setProtectMatcher();
        this.setProtectMatcherPermission();
    }

    private void setProtectMatcher() {
        List<ServerWebExchangeMatcher> permitMatchers = this.resourceService.listPermitResources()
                .stream()
                .flatMap(resource -> this.resourceToMatchers(resource).stream())
                .collect(Collectors.toList());
        OrServerWebExchangeMatcher publicMatchers = new OrServerWebExchangeMatcher(permitMatchers);
        this.protectMatcher.setMatcher(new NegatedServerWebExchangeMatcher(publicMatchers));
    }

    private void setProtectMatcherPermission() {
        Map<ServerWebExchangeMatcher, AuthResourcePermission> permissions = Maps.newHashMap();
        this.resourceService.listProtectResourcePermission().forEach((resource, permission) -> {
            this.resourceToMatchers(resource).forEach((matcher -> {
                permissions.put(matcher, permission);
            }));
        });
        this.protectMatcherPermission = permissions;
    }

    private List<ServerWebExchangeMatcher> resourceToMatchers(AuthResource resource) {
        String path = resource.getPath();
        if (!StringUtils.hasText(path)) {
            return Collections.emptyList();
        }
        return resource.getMethods()
                .stream()
                .map(method -> {
                    HttpMethod httpMethod = HttpMethod.resolve(method);
                    return ServerWebExchangeMatchers.pathMatchers(httpMethod, path);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void onResourceRefreshed() {
        this.init();
    }

    private static class DefaultSecurityExpressionRoot extends SecurityExpressionRoot {
        DefaultSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }
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
