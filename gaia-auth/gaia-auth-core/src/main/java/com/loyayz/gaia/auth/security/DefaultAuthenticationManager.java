package com.loyayz.gaia.auth.security;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
@RequiredArgsConstructor
public class DefaultAuthenticationManager implements AuthenticationManager {
    private final AuthUserCache authUserCache;

    private UserDetailsChecker authenticationChecks = new AccountStatusUserDetailsChecker();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**
     * 鉴权
     *
     * @param authentication AuthCredentialsToken
     * @return AuthCredentialsToken. Principal from {@link AuthUserCache#getUserFromCache(AuthCredentials)}
     * @throws AuthenticationException 鉴权异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthCredentials credentials = ((AuthenticationCredentialsToken) authentication).getCredentials();
        this.validBeforeAuth(credentials);
        SecurityUserDetails user = this.retrieveUser(credentials);
        return this.createSuccessAuthentication(credentials, user);
    }

    private SecurityUserDetails retrieveUser(AuthCredentials credentials) {
        AuthUser user;
        try {
            user = this.authUserCache.getUserFromCache(credentials);
        } catch (Exception e) {
            if (AuthenticationException.class.isAssignableFrom(e.getClass())) {
                throw e;
            }
            throw new BadCredentialsException("Bad credentials [" + credentials + "]", e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Could not find resource user from credentials [" + credentials + "]");
        }

        SecurityUserDetails userDetails = new SecurityUserDetails(user);
        this.authenticationChecks.check(userDetails);
        return userDetails;
    }

    private void validBeforeAuth(AuthCredentials credentials) {
        if (credentials == null || !StringUtils.hasText(credentials.getToken())) {
            throw new AuthenticationCredentialsNotFoundException("Could not find original Authentication object");
        }
    }

    private Authentication createSuccessAuthentication(AuthCredentials credentials, SecurityUserDetails userDetails) {
        return new AuthenticationCredentialsToken(userDetails.getUser(), credentials,
                this.authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
    }

    @Data
    @RequiredArgsConstructor
    private static class SecurityUserDetails implements UserDetails, CredentialsContainer {
        private static final long serialVersionUID = -1L;
        private final String DEFAULT_ROLE_PREFIX = "ROLE_";

        private final AuthUser user;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getUser().getRoles()
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
            return getUser().getName();
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

}
