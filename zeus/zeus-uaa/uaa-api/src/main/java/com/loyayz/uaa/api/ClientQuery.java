package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.ClientQueryParam;
import com.loyayz.uaa.dto.ClientSecret;
import com.loyayz.uaa.dto.SimpleClient;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ClientQuery {

    /**
     * 根据id查询客户端
     */
    SimpleClient getClient(Long clientId);

    /**
     * 分页查询客户端
     */
    PageModel<SimpleClient> pageClient(ClientQueryParam queryParam, PageRequest pageRequest);

    /**
     * 获取客户端密钥
     */
    ClientSecret getSecret(Long clientId);

    /**
     * 根据客户端密钥解密
     *
     * @param clientId  客户端 id
     * @param encrypted 加密的数据
     */
    String decryptSecret(Long clientId, String encrypted);

    /**
     * 生成密钥
     *
     * @param secretType 密钥类型
     */
    ClientSecret generateSecret(String secretType);

}
