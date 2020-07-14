package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.SimpleApp;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AppManager {

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
