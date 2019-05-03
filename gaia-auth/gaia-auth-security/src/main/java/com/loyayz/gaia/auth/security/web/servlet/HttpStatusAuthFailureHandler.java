package com.loyayz.gaia.auth.security.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class HttpStatusAuthFailureHandler implements AuthenticationEntryPoint, AuthenticationFailureHandler, AccessDeniedHandler {
    private final HttpStatus httpStatus;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        this.responseError(request, response, exception);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        this.responseError(request, response, exception);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
            throws IOException, ServletException {
        this.responseError(request, response, exception);
    }

    private void responseError(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(this.httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String responseBody = this.responseBody(request, exception);
        response.getWriter().write(responseBody);
    }

    private String responseBody(HttpServletRequest request, Exception exception) {
        String result;
        try {
            Map<String, Object> param = new LinkedHashMap<>(8);
            param.put("status", this.httpStatus.value());
            param.put("code", this.httpStatus.value());
            param.put("message", exception.getMessage());
            param.put("path", request.getRequestURI());
            result = objectMapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
