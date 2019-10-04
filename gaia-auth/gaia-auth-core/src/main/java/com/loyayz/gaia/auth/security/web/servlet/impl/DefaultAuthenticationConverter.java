package com.loyayz.gaia.auth.security.web.servlet.impl;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
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

    public DefaultAuthenticationConverter(AuthCredentialsConfiguration credentialsConfiguration) {
        this.authCredentialsExtractor = new ServletAuthCredentialsExtractor(credentialsConfiguration);
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        AuthCredentials credentials = this.authCredentialsExtractor.extract(request);
        return new AuthenticationCredentialsToken(credentials);
    }

}
