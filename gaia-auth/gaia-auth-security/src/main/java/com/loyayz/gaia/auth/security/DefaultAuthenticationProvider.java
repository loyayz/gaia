package com.loyayz.gaia.auth.security;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import com.loyayz.gaia.auth.core.user.AuthUserExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
@RequiredArgsConstructor
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    private final AuthUserExtractor authUserExtractor;

    private UserDetailsChecker authenticationChecks = new DefaultAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**
     * 鉴权
     *
     * @param authentication 来自 {@link com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor}
     * @return Authentication. Principal from {@link AuthUserExtractor}
     * @throws AuthenticationException 鉴权异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthCredentials credentials = ((AuthenticationCredentialsToken) authentication).getCredentials();
        this.validBeforeAuth(credentials);
        SecurityUserDetails user = this.retrieveUser(credentials);
        return this.createSuccessAuthentication(credentials, user);
    }

    protected SecurityUserDetails retrieveUser(AuthCredentials credentials) {
        AuthUser user;
        try {
            user = this.authUserExtractor.extract(credentials);
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

    protected void validBeforeAuth(AuthCredentials credentials) {
        if (credentials == null || !StringUtils.hasText(credentials.getToken())) {
            throw new AuthenticationCredentialsNotFoundException("Could not find original Authentication object");
        }
    }

    protected Authentication createSuccessAuthentication(AuthCredentials credentials, SecurityUserDetails userDetails) {
        return new AuthenticationCredentialsToken(userDetails.getUser(), credentials,
                this.authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationCredentialsToken.class.isAssignableFrom(authentication);
    }

    public class DefaultAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                throw new LockedException("User account is locked");
            }

            if (!user.isEnabled()) {
                throw new DisabledException("User is disabled");
            }

            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("User account has expired");
            }

            if (!user.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("User credentials have expired");
            }
        }

    }

}
