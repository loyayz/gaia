package com.loyayz.gaia.auth.security;

import com.loyayz.gaia.auth.core.user.AuthUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@RequiredArgsConstructor
public class AuthUserDetails implements UserDetails, CredentialsContainer {
    private static final long serialVersionUID = -1L;
    private final String DEFAULT_ROLE_PREFIX = "ROLE_";

    private final AuthUser user;

    public AuthUser me() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return me().getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(DEFAULT_ROLE_PREFIX + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return me().getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {

    }

}
