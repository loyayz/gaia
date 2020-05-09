package com.loyayz.gaia.auth.core.identity.impl;

import com.loyayz.gaia.auth.core.authentication.AuthCredentials;
import com.loyayz.gaia.auth.core.identity.AuthIdentity;
import com.loyayz.gaia.auth.core.identity.AuthIdentityCache;
import com.loyayz.gaia.auth.core.identity.AuthIdentityCacheDataConverter;
import com.loyayz.gaia.auth.core.identity.AuthIdentityService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultAuthIdentityService implements AuthIdentityService {
    private final AuthIdentityCache identityCache;
    private final AuthIdentityCacheDataConverter identityCacheDataConverter;

    @Override
    public AuthIdentity retrieve(AuthCredentials credentials) {
        String token = credentials.getToken();
        Map<String, Object> item = this.identityCache.get(token);
        return this.identityCacheDataConverter.toIdentity(item);
    }

    @Override
    public AuthCredentials store(AuthIdentity identity) {
        Map<String, Object> item = this.identityCacheDataConverter.toCacheData(identity);
        String token = this.identityCache.put(identity.getId(), item);
        return new AuthCredentials(token);
    }

    @Override
    public void remove(AuthCredentials credentials) {
        String token = credentials.getToken();
        this.identityCache.remove(token);
    }

}
