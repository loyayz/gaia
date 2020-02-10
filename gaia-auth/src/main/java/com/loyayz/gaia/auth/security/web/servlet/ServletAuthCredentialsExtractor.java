package com.loyayz.gaia.auth.security.web.servlet;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.credentials.AbstractAuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class ServletAuthCredentialsExtractor extends AbstractAuthCredentialsExtractor<HttpServletRequest>
        implements AuthCredentialsExtractor<HttpServletRequest> {

    public ServletAuthCredentialsExtractor(AuthCredentialsProperties credentialsProperties) {
        super(credentialsProperties);
    }

    @Override
    protected String getHeaderToken(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    @Override
    protected String getParamToken(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

}
