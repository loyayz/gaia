package com.loyayz.gaia.auth.security.web.servlet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

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
public class AuthenticationFilter extends OncePerRequestFilter {
    private RequestMatcher requestMatcher;
    private AuthenticationManager authenticationManager;
    private AuthenticationConverter authenticationConverter;

    private AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!this.requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
                filterChain.doFilter(request, response);
                return;
            }

            successfulAuthentication(request, response, filterChain, authenticationResult);
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
        }
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        chain.doFilter(request, response);
    }

    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, ServletException {

        Authentication authentication = this.authenticationConverter.convert(request, response);
        if (authentication == null) {
            return null;
        }

        Authentication authenticationResult = this.getAuthenticationManager().authenticate(authentication);
        if (authenticationResult == null) {
            throw new ServletException("AuthenticationManager should not return null Authentication object.");
        }

        return authenticationResult;
    }

    private AccessDeniedException permissionException(Authentication authentication, HttpServletRequest request) {
        String path = request.getContextPath();
        return new AccessDeniedException("No permission to visit " + path + " with [" + authentication + "] ");
    }

}
