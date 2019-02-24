package com.loyayz.gaia.auth.core.user;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultAuthUserExtractor implements AuthUserExtractor {
    @Getter(AccessLevel.PROTECTED)
    private final AuthUserCache authUserCache;

    @Override
    public AuthUser extract(AuthCredentials credentials) {
        return this.authUserCache.getUserFromCache(credentials);
    }

}
