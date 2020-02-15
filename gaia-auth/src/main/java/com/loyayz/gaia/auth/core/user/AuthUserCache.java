package com.loyayz.gaia.auth.core.user;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthUserCache {

    /**
     * 获取缓存的用户信息
     *
     * @param token cacheKey
     * @return 查无缓存或缓存过期，则返回 null
     */
    Map<String, Object> get(String token);

    /**
     * 缓存用户信息
     *
     * @param userId 用户id
     * @param user   要缓存的用户详情
     * @return token cacheKey
     */
    String put(String userId, Map<String, Object> user);

    /**
     * 删除缓存
     *
     * @param token cacheKey
     */
    void remove(String token);

}
