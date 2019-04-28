package com.loyayz.gaia.auth.security.web;

import com.loyayz.gaia.commons.exception.ExceptionDefiner;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AuthenticationExceptionDefiner implements ExceptionDefiner {
    public static final String BEAN_NAME = "authenticationExceptionDefiner";

    @Override
    public List<Class<? extends Throwable>> exceptions() {
        return Collections.singletonList(AuthenticationException.class);
    }

    @Override
    public String code(Throwable e) {
        return String.valueOf(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public int status(Throwable e) {
        return HttpStatus.UNAUTHORIZED.value();
    }

}
