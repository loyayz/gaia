package com.loyayz.gaia.auth.core.identity;

import com.loyayz.gaia.auth.core.authentication.AuthCredentials;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthIdentityService {

    /**
     * 提取 token 对应的身份信息
     *
     * @param credentials token
     * @return 身份信息
     */
    AuthIdentity retrieve(AuthCredentials credentials);

    /**
     * 存储身份信息
     *
     * @param identity 身份信息
     * @return token
     */
    AuthCredentials store(AuthIdentity identity);

    /**
     * 删除存储的身份信息
     *
     * @param credentials token
     */
    void remove(AuthCredentials credentials);

}
