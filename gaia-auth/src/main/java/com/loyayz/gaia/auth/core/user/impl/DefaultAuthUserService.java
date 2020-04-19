package com.loyayz.gaia.auth.core.user.impl;

import com.loyayz.gaia.auth.core.authentication.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import com.loyayz.gaia.auth.core.user.AuthUserCacheItemConverter;
import com.loyayz.gaia.auth.core.user.AuthUserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class DefaultAuthUserService implements AuthUserService {
    private final AuthUserCache userCache;
    private final AuthUserCacheItemConverter userCacheItemConverter;

    @Override
    public AuthUser retrieve(AuthCredentials credentials) {
        String token = credentials.getToken();
        Map<String, Object> item = this.userCache.get(token);
        return this.userCacheItemConverter.toUser(item);
    }

    @Override
    public AuthCredentials store(AuthUser user) {
        Map<String, Object> item = this.userCacheItemConverter.toCacheItem(user);
        String token = this.userCache.put(user.getId(), item);
        return new AuthCredentials(token);
    }

    @Override
    public void remove(AuthCredentials credentials) {
        String token = credentials.getToken();
        this.userCache.remove(token);
    }

}
