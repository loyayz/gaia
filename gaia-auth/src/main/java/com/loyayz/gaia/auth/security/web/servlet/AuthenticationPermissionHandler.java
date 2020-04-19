package com.loyayz.gaia.auth.security.web.servlet;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthenticationPermissionHandler {

    void refresh();

    /**
     * 需要鉴权的校验器
     */
    RequestMatcher requiresAuthenticationMatcher();

    /**
     * 是否需要鉴权
     */
    default Boolean requiresAuthentication(HttpServletRequest request) {
        return this.requiresAuthenticationMatcher().matches(request);
    }

    /**
     * 鉴权
     */
    Boolean hasPermission(Authentication authentication, HttpServletRequest request);

}
