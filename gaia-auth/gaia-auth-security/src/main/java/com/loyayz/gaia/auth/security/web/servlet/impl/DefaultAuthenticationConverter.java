package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.security.AuthenticationCredentialsToken;
import com.loyayz.gaia.auth.security.web.servlet.AuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultAuthenticationConverter implements AuthenticationConverter {
    private final AuthCredentialsExtractor<HttpServletRequest> authCredentialsExtractor;

    @Override
    public Authentication convert(HttpServletRequest request, HttpServletResponse response) {
        AuthCredentials credentials = this.authCredentialsExtractor.extract(request);
        return new AuthenticationCredentialsToken(credentials);
    }

}
