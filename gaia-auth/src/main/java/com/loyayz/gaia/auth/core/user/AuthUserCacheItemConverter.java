package com.loyayz.gaia.auth.core.user;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthUserCacheItemConverter {

    /**
     * 用户信息转为缓存对象
     *
     * @param user 用户信息
     * @return 缓存的对象
     * @see AuthUserCache
     */
    Map<String, Object> toCacheItem(AuthUser user);

    /**
     * 缓存对象转为用户信息
     *
     * @param user 缓存的对象
     * @return 用户信息
     * @see AuthUserCache
     */
    AuthUser toUser(Map<String, Object> user);

}
