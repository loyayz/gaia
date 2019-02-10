package com.loyayz.gaia.auth.core.user;

import com.loyayz.gaia.auth.core.credentials.AuthenticationCredentials;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@FunctionalInterface
public interface SecurityUserExtractor {

    /**
     * 从凭证中取回用户信息
     *
     * @param credentials The credentials to retrieve user
     * @return 用户信息
     */
    SecurityUser extract(AuthenticationCredentials credentials);

}
