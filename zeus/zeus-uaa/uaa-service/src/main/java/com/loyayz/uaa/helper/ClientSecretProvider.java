package com.loyayz.uaa.helper;

import com.loyayz.uaa.dto.ClientSecret;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ClientSecretProvider {

    /**
     * 密钥类型
     */
    String secretType();

    /**
     * 解密
     *
     * @param privateKey 私钥
     * @param encrypted  加密的数据
     */
    String decrypt(String privateKey, String encrypted);

    /**
     * 生成密钥
     */
    ClientSecret generateSecret();

    /**
     * 是否适用该密钥类型
     */
    default boolean support(String secretType) {
        return this.secretType().equalsIgnoreCase(secretType);
    }

}
