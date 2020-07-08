package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.data.UaaClientApp;
import com.loyayz.uaa.dto.ClientQueryParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class ClientRepository {

    public static UaaClient findById(Long clientId) {
        return new UaaClient().findById(clientId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaClientMapper#listByParam}
     */
    public static List<UaaClient> listByParam(ClientQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaClient.class, "listByParam", queryParam);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaClientAppMapper#listAppByClient}
     */
    public static List<Long> listAppIds(Long clientId) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("clientId", clientId);
        return MybatisUtils.executeSelectList(UaaClientApp.class, "listAppByClient", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaClientAppMapper#listAppByClientApps}
     */
    public static List<Long> listAppIds(Long clientId, Collection<Long> appIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("clientId", clientId);
        param.put("appIds", appIds);
        return MybatisUtils.executeSelectList(UaaClientApp.class, "listAppByClientApps", param);
    }

}
