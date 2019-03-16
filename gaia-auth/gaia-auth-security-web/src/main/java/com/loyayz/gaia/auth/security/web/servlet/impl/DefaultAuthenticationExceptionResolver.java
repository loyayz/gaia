package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.security.web.servlet.AuthenticationExceptionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Slf4j
public class DefaultAuthenticationExceptionResolver implements AuthenticationExceptionResolver {

    @Override
    public void resolve(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        log.error("authentication fail.", exception);

        int status = HttpStatus.UNAUTHORIZED.value();
        if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN.value();
        }
        response.setStatus(status);
    }

}
