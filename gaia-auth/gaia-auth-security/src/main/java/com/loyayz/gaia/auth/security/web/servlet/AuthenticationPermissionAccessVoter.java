package com.loyayz.gaia.auth.security.web.servlet;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class AuthenticationPermissionAccessVoter implements AccessDecisionVoter<FilterInvocation> {
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

    public AccessDecisionManager defaultManager() {
        return new AffirmativeBased(Collections.singletonList(this));
    }

}
