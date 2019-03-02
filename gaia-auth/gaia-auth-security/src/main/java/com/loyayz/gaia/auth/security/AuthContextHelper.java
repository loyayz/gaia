package com.loyayz.gaia.auth.security;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AuthContextHelper {

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static Optional<AuthenticationCredentialsToken> getAuthentication() {
        Authentication authentication = getSecurityContext().getAuthentication();
        if (!(authentication instanceof AuthenticationCredentialsToken)) {
            authentication = null;
        }
        return Optional.ofNullable((AuthenticationCredentialsToken) authentication);
    }

    /**
     * 获取当前用户
     *
     * @see DefaultAuthenticationProvider
     */
    public static AuthUser getUser() {
        return getAuthentication()
                .map(AuthenticationCredentialsToken::getPrincipal)
                .orElse(null);
    }

    /**
     * 当前用户请求携带的 token
     *
     * @see DefaultAuthenticationProvider
     */
    public static AuthCredentials getCredentials() {
        return getAuthentication()
                .map(AuthenticationCredentialsToken::getCredentials)
                .orElse(null);
    }

    /**
     * 当前用户请求携带的 token
     */
    public static String getToken() {
        AuthCredentials credentials = getCredentials();
        return credentials == null ? null : credentials.getToken();
    }

    /**
     * 获取当前用户的id
     */
    public static String getUserId() {
        AuthUser user = getUser();
        return user == null ? null : user.getId();
    }

    /**
     * 获取当前用户名
     */
    public static String getUserName() {
        AuthUser user = getUser();
        return user == null ? null : user.getName();
    }

    /**
     * 获取当前用户的角色
     */
    public static List<String> getUserRoles() {
        AuthUser user = getUser();
        return user == null ? Collections.emptyList() : user.getRoles();
    }

    private AuthContextHelper() {

    }
}
