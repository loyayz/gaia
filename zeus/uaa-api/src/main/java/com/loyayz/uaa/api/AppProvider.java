package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.*;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AppProvider {

    /**
     * 获取应用信息
     */
    SimpleApp getApp(Long appId);

    /**
     * 查询应用信息列表
     *
     * @param queryParam 查询条件
     */
    List<SimpleApp> listApp(AppQueryParam queryParam);

    /**
     * 查询应用菜单列表
     *
     * @param queryParam 查询条件
     */
    List<SimpleMenu> listMenu(MenuQueryParam queryParam);

    /**
     * 查询应用菜单列表，并构造为树形结果
     *
     * @param appId 应用
     */
    List<SimpleMenu> listMenuTree(Long appId);

    /**
     * 查询应用菜单功能列表
     *
     * @param appId 应用
     */
    List<SimpleMenuAction> listAppAction(Long appId);

    /**
     * 查询应用菜单功能列表
     *
     * @param menuCode 菜单编码
     */
    List<SimpleMenuAction> listMenuAction(String menuCode);

}
