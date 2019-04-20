package com.loyayz.gaia.auth.core.security;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AuthenticationCredentialsToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private AuthUser principal;
    private AuthCredentials credentials;

    public AuthenticationCredentialsToken(AuthCredentials credentials) {
        this(null, credentials);
    }

    public AuthenticationCredentialsToken(AuthUser principal, AuthCredentials credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    public AuthenticationCredentialsToken(AuthUser principal, AuthCredentials credentials,
                                          Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public AuthUser getPrincipal() {
        return this.principal;
    }

    @Override
    public AuthCredentials getCredentials() {
        return this.credentials;
    }

}
