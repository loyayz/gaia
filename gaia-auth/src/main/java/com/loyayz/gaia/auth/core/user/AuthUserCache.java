package com.loyayz.gaia.auth.core.user;

import com.loyayz.gaia.auth.core.credentials.AuthCredentials;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthUserCache {

    /**
     * 获取缓存的用户详情
     *
     * @param credentials {@link #putUserInCache result}
     * @return 查无缓存或缓存过期，则返回 null
     */
    AuthUser getUserFromCache(AuthCredentials credentials);

    /**
     * 缓存用户详情
     *
     * @param user 要缓存的用户详情
     * @return token
     */
    AuthCredentials putUserInCache(AuthUser user);

    /**
     * 删除缓存
     *
     * @param credentials {@link #putUserInCache result}
     */
    void removeUserFromCache(AuthCredentials credentials);

}
