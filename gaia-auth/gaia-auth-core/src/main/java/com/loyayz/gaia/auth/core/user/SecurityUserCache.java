package com.loyayz.gaia.auth.core.user;

import com.loyayz.gaia.auth.core.credentials.AuthenticationCredentials;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface SecurityUserCache {

    /**
     * 获取缓存的用户详情
     *
     * @param credentials {@link #putUserInCache result}
     * @return 查无缓存或缓存过期，则返回 null
     */
    SecurityUser getUserFromCache(AuthenticationCredentials credentials);

    /**
     * 缓存用户详情
     *
     * @param user 要缓存的用户详情
     * @return token
     */
    AuthenticationCredentials putUserInCache(SecurityUser user);

    /**
     * 删除缓存
     *
     * @param credentials {@link #putUserInCache result}
     */
    default void removeUserFromCache(AuthenticationCredentials credentials) {
        this.removeUserFromCache(credentials.getToken());
    }

    /**
     * 删除缓存
     *
     * @param token {@link #removeUserFromCache(AuthenticationCredentials)}
     */
    void removeUserFromCache(String token);

}
