package com.loyayz.gaia.auth.security.web.servlet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private AuthenticationConverter authenticationConverter;
    private AuthenticationPermissionHandler authenticationPermissionHandler;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationPermissionHandler authenticationPermissionHandler) {
        super(authenticationPermissionHandler.requiresAuthenticationMatcher());
        super.setAuthenticationManager(authenticationManager);
        this.authenticationPermissionHandler = authenticationPermissionHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = this.authenticationConverter.convert(request, response);
        return super.getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        boolean hasPermission = this.authenticationPermissionHandler.hasPermission(authentication, request);
        if (!hasPermission) {
            throw this.permissionException(authentication, request);
        }
        super.successfulAuthentication(request, response, chain, authentication);
        chain.doFilter(request, response);
    }

    private AccessDeniedException permissionException(Authentication authentication, HttpServletRequest request) {
        String path = request.getContextPath();
        return new AccessDeniedException("No permission to visit " + path + " with [" + authentication + "] ");
    }

}
