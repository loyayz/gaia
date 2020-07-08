package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.ClientSecret;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ClientSecretManager {

    /**
     * 获取客户端密钥
     */
    ClientSecret getSecret(Long clientId);

    /**
     * 重置客户端密钥
     *
     * @param clientId   客户端 id
     * @param secretType 密钥类型
     */
    void resetSecret(Long clientId, String secretType);

    /**
     * 解密
     *
     * @param clientId  客户端 id
     * @param encrypted 加密的数据
     */
    String decrypt(Long clientId, String encrypted);

    /**
     * 生成密钥
     *
     * @param secretType 密钥类型
     */
    ClientSecret generateSecret(String secretType);

}
