package com.loyayz.gaia.auth.security.web.servlet;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@FunctionalInterface
public interface AuthenticationConverter {

    /**
     * Converts a {@link HttpServletRequest} to an {@link Authentication}
     *
     * @param request  The {@link HttpServletRequest}
     * @param response The {@link HttpServletResponse}
     * @return an {@link Authentication}
     */
    Authentication convert(HttpServletRequest request, HttpServletResponse response);

}
