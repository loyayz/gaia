package com.loyayz.gaia.auth.core.identity;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthIdentityCacheDataConverter {

    /**
     * 身份信息转为缓存数据
     *
     * @param identity 身份信息
     * @return 缓存的数据
     * @see AuthIdentityCache
     */
    Map<String, Object> toCacheData(AuthIdentity identity);

    /**
     * 缓存对象转为身份信息
     *
     * @param identity 缓存的数据
     * @return 身份信息
     * @see AuthIdentityCache
     */
    AuthIdentity toIdentity(Map<String, Object> identity);

}
