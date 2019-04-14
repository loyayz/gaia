package com.loyayz.gaia.auth.security.web;

import com.google.common.collect.Lists;
import com.loyayz.gaia.exception.core.ExceptionDefiner;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AccessDeniedExceptionDefiner implements ExceptionDefiner {
    public static final String BEAN_NAME = "accessDeniedExceptionDefiner";

    @Override
    public List<Class<? extends Throwable>> exceptions() {
        return Lists.newArrayList(AccessDeniedException.class);
    }

    @Override
    public String code(Throwable e) {
        return String.valueOf(HttpStatus.FORBIDDEN.value());
    }

    @Override
    public int status(Throwable e) {
        return HttpStatus.FORBIDDEN.value();
    }

}
