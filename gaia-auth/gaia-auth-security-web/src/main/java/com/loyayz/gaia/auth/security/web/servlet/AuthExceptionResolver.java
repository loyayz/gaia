package com.loyayz.gaia.auth.security.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthExceptionResolver {

    void resolve(HttpServletRequest request, HttpServletResponse response, Throwable exception);

}
