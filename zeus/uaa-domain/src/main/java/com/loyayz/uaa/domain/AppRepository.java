package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.Sorter;
import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.*;
import com.loyayz.uaa.dto.AppQueryParam;
import com.loyayz.uaa.dto.MenuQueryParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class AppRepository {

    public static UaaApp findById(Long appId) {
        return new UaaApp().findById(appId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMapper#getMaxSort}
     */
    public static Integer getAppNextSort() {
        Integer sort = MybatisUtils.executeSelectOne(UaaApp.class, "getMaxSort", new Object());
        return sort == null ? 0 : sort + 1;
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMapper#listByParam}
     */
    public static List<UaaApp> listAppByParam(AppQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaApp.class, "listByParam", queryParam);
    }

    public static List<Long> listUserIdByApp(Long appId) {
        return UaaAppAdmin.builder().appId(appId).build()
                .listByCondition()
                .stream()
                .map(UaaAppAdmin::getUserId)
                .collect(Collectors.toList());
    }

    public static UaaAppMenu getAppMenuByCode(String code) {
        return UaaAppMenu.builder().code(code).build()
                .listByCondition()
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMapper#listByParam}
     */
    public static List<UaaAppMenu> listAppMenuByParam(MenuQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaAppMenu.class, "listByParam", queryParam);
    }

    public static Integer countAppMenuByParent(String parentCode) {
        return UaaAppMenu.builder().parentCode(parentCode).build()
                .countByCondition();
    }

    public static List<UaaAppMenuAction> listAppMenuActionByApp(Long appId) {
        return UaaAppMenuAction.builder().appId(appId).build()
                .listByCondition(Sorter.asc("sort"));
    }

    public static List<UaaAppMenuAction> listAppMenuActionByMenu(String menuCode) {
        return UaaAppMenuAction.builder().menuCode(menuCode).build()
                .listByCondition(Sorter.asc("sort"));
    }

}
