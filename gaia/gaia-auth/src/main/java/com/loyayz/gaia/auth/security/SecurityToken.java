package com.loyayz.gaia.auth.security;

import com.loyayz.gaia.auth.core.authentication.AuthCredentials;
import com.loyayz.gaia.auth.core.identity.AuthIdentity;
import com.loyayz.gaia.auth.core.identity.AuthIdentityRole;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class SecurityToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private AuthIdentity principal;
    private AuthCredentials credentials;

    public SecurityToken(AuthCredentials credentials) {
        this(null, credentials);
    }

    public SecurityToken(AuthIdentity principal, AuthCredentials credentials) {
        super(toAuthorities(principal));
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(principal != null);
    }

    @Override
    public AuthIdentity getPrincipal() {
        return this.principal;
    }

    @Override
    public AuthCredentials getCredentials() {
        return this.credentials;
    }

    public List<AuthIdentityRole> getRoles() {
        return this.principal == null ?
                Collections.emptyList() : this.principal.getRoles();
    }

    public List<String> getRoleCodes() {
        return this.getRoles().stream()
                .map(AuthIdentityRole::getCode)
                .collect(Collectors.toList());
    }

    public boolean hasAnyRole(List<String> roles) {
        if (roles == null) {
            return false;
        }
        List<String> roleCodes = this.getRoleCodes();
        for (String role : roles) {
            if (roleCodes.contains(role)) {
                return true;
            }
        }
        return false;
    }

    private static List<GrantedAuthority> toAuthorities(AuthIdentity identity) {
        return identity == null ? null :
                identity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                        .collect(Collectors.toList());
    }

}
