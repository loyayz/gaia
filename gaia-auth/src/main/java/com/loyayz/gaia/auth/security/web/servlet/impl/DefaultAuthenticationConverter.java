package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.security.AuthenticationCredentialsToken;
import com.loyayz.gaia.auth.security.web.servlet.ServletAuthCredentialsExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class DefaultAuthenticationConverter implements AuthenticationConverter {
    private final ServletAuthCredentialsExtractor authCredentialsExtractor;

    public DefaultAuthenticationConverter(AuthCredentialsProperties credentialsProperties) {
        this.authCredentialsExtractor = new ServletAuthCredentialsExtractor(credentialsProperties);
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        AuthCredentials credentials = this.authCredentialsExtractor.extract(request);
        return new AuthenticationCredentialsToken(credentials);
    }

}
