package com.loyayz.gaia.auth.core.credentials;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@FunctionalInterface
public interface SecurityAuthCredentialsExtractor<T> {

    /**
     * 提取用于鉴权的凭证
     *
     * @param request 要提取凭证的请求
     * @return credentials never null
     */
    AuthenticationCredentials extract(T request);

}
