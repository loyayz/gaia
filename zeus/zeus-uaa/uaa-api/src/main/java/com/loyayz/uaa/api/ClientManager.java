package com.loyayz.uaa.api;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ClientManager {

    /**
     * 新增客户端
     *
     * @param clientName 名称
     * @param secretType 密钥类型
     * @return 客户端 id
     */
    Long addClient(String clientName, String secretType);

    /**
     * 修改客户端
     *
     * @param clientId   客户端 id
     * @param clientName 名称
     */
    void updateClient(Long clientId, String clientName);

    /**
     * 删除客户端
     */
    void deleteClient(List<Long> clientIds);

    /**
     * 重置客户端密钥
     *
     * @param clientId   客户端 id
     * @param secretType 密钥类型
     */
    void resetSecret(Long clientId, String secretType);

    /**
     * 客户端添加应用
     */
    void addApp(Long clientId, List<Long> appIds);

    /**
     * 客户端删除应用
     */
    void removeApp(Long clientId, List<Long> appIds);

}
