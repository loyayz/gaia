package com.loyayz.gaia.auth.security.web.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@AllArgsConstructor
public class DelegatingRequestMatcher implements RequestMatcher {

    private RequestMatcher matcher;

    @Override
    public boolean matches(HttpServletRequest request) {
        return this.matcher.matches(request);
    }

}
