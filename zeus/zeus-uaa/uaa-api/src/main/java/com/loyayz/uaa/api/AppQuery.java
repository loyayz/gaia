package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.AppQueryParam;
import com.loyayz.uaa.dto.SimpleApp;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AppQuery {

    /**
     * 根据id查询应用
     */
    SimpleApp getApp(Long appId);

    /**
     * 分页查询应用
     */
    PageModel<SimpleApp> pageApp(AppQueryParam queryParam, PageRequest pageRequest);

    /**
     * 应用列表
     */
    List<SimpleApp> listApp(List<Long> appIds);

}
