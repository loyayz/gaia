package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.AppQueryParam;
import com.loyayz.uaa.dto.SimpleApp;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AppService {

    /**
     * 根据id查询应用
     */
    SimpleApp getApp(Long appId);

    /**
     * 分页查询应用
     */
    PageModel<SimpleApp> pageApp(AppQueryParam queryParam, PageRequest pageRequest);

    /**
     * 新增应用
     *
     * @param app 应用信息
     * @return 应用 id
     */
    Long addApp(SimpleApp app);

    /**
     * 修改应用
     */
    void updateApp(Long appId, SimpleApp app);

    /**
     * 删除应用
     */
    void deleteApp(List<Long> appIds);

}
