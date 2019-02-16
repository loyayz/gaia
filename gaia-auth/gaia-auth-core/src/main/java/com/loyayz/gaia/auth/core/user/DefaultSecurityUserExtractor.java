package com.loyayz.gaia.auth.core.user;

import com.loyayz.gaia.auth.core.credentials.AuthenticationCredentials;
import lombok.RequiredArgsConstructor;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultSecurityUserExtractor implements SecurityUserExtractor {
    private final SecurityUserCache securityUserCache;

    @Override
    public SecurityUser extract(AuthenticationCredentials credentials) {
        return this.securityUserCache.getUserFromCache(credentials);
    }

}
