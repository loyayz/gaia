package com.loyayz.gaia.auth.core.identity;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthIdentityCache {

    /**
     * 获取缓存的身份信息
     *
     * @param token cacheKey
     * @return 查无缓存或缓存过期，则返回 null
     */
    Map<String, Object> get(String token);

    /**
     * 缓存身份信息
     *
     * @param identityId 身份id
     * @param identity   要缓存的身份详情
     * @return token cacheKey
     */
    String put(String identityId, Map<String, Object> identity);

    /**
     * 删除缓存
     *
     * @param token cacheKey
     */
    void remove(String token);

}
