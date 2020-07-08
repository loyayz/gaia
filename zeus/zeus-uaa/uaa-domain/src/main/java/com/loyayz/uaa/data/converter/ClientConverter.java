package com.loyayz.uaa.data.converter;

import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.dto.ClientSecret;
import com.loyayz.uaa.dto.SimpleClient;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class ClientConverter {

    /**
     * 转为 dto
     * 不返回私钥
     */
    public static SimpleClient toSimple(UaaClient client) {
        ClientSecret secret = toSecret(client);
        secret.setPrivateKey("");

        SimpleClient result = new SimpleClient();
        result.setId(client.getId());
        result.setName(client.getName());
        result.setSecret(secret);
        result.setCreateTime(client.getGmtCreate().getTime());
        return result;
    }

    public static ClientSecret toSecret(UaaClient client) {
        ClientSecret secret = new ClientSecret();
        secret.setType(client.getSecretType());
        secret.setPublicKey(client.getPublicKey());
        secret.setPrivateKey(client.getPrivateKey());
        return secret;
    }

}
