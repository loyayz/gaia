package com.loyayz.gaia.auth.security;

import com.loyayz.gaia.auth.core.authentication.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import com.loyayz.gaia.auth.core.user.AuthUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
@RequiredArgsConstructor
public class DefaultAuthenticationManager implements AuthenticationManager {
    private final AuthUserService authUserService;

    /**
     * 鉴权
     *
     * @param authentication {@link SecurityToken}
     * @return {@link SecurityToken}. Principal from {@link AuthUserService#retrieve(AuthCredentials)}
     * @throws AuthenticationException 鉴权异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthCredentials credentials = ((SecurityToken) authentication).getCredentials();
        this.validBeforeAuth(credentials);
        AuthUser user = this.retrieveUser(credentials);
        return new SecurityToken(user, credentials);
    }

    private AuthUser retrieveUser(AuthCredentials credentials) {
        AuthUser user;
        try {
            user = this.authUserService.retrieve(credentials);
        } catch (Exception e) {
            if (AuthenticationException.class.isAssignableFrom(e.getClass())) {
                throw e;
            }
            throw new BadCredentialsException("Bad credentials [" + credentials + "]", e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Could not find resource user from credentials [" + credentials + "]");
        }
        return user;
    }

    private void validBeforeAuth(AuthCredentials credentials) {
        if (credentials == null || !StringUtils.hasText(credentials.getToken())) {
            throw new AuthenticationCredentialsNotFoundException("Could not find original Authentication object");
        }
    }

}
