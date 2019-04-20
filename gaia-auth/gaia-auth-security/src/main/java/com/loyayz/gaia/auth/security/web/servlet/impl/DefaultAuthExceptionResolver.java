package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.security.web.servlet.AuthExceptionResolver;
import com.loyayz.gaia.exception.core.ExceptionResult;
import com.loyayz.gaia.exception.core.WebmvcExceptionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultAuthExceptionResolver implements AuthExceptionResolver {
    private final WebmvcExceptionResolver resolver;

    @Override
    public void resolve(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        ExceptionResult result = resolver.handlerException(request, response, exception);
        String resultContent = result.toJsonString();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            response.getWriter().write(resultContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
