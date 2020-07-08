package com.loyayz.uaa.service;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.loyayz.uaa.dto.ClientSecret;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@Order
public class RsaClientSecretProvider implements ClientSecretProvider {

    @Override
    public String secretType() {
        return "RSA";
    }

    @Override
    public String decrypt(String privateKey, String encrypted) {
        RSA rsa = new RSA(privateKey, null);
        return rsa.decryptStr(encrypted, KeyType.PrivateKey);
    }

    @Override
    public ClientSecret generateSecret() {
        RSA rsa = new RSA();
        ClientSecret secret = new ClientSecret();
        secret.setType(this.secretType());
        secret.setPublicKey(rsa.getPublicKeyBase64());
        secret.setPrivateKey(rsa.getPrivateKeyBase64());
        return secret;
    }

    @Override
    public boolean support(String secretType) {
        return true;
    }

}
