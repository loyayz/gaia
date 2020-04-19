package com.loyayz.gaia.auth.core.user;

import com.loyayz.gaia.auth.core.authentication.AuthCredentials;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthUserService {

    /**
     * 提取 token 对应的用户信息
     *
     * @param credentials token
     * @return 用户信息
     */
    AuthUser retrieve(AuthCredentials credentials);

    /**
     * 存储用户信息
     *
     * @param user 用户信息
     * @return token
     */
    AuthCredentials store(AuthUser user);

    /**
     * 删除存储的用户信息
     *
     * @param credentials token
     */
    void remove(AuthCredentials credentials);

}
