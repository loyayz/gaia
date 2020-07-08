package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.ClientQueryParam;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleClient;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ClientService {

    /**
     * 根据id查询客户端
     */
    SimpleClient getClient(Long clientId);

    /**
     * 分页查询客户端
     */
    PageModel<SimpleClient> pageClient(ClientQueryParam queryParam, PageRequest pageRequest);

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
     * 查询客户端拥有的应用
     */
    List<SimpleApp> listAppByClient(Long clientId);

    /**
     * 客户端添加应用
     */
    void addApp(Long clientId, List<Long> appIds);

    /**
     * 客户端删除应用
     */
    void removeApp(Long clientId, List<Long> appIds);

}
