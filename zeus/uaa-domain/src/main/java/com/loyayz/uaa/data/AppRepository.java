package com.loyayz.uaa.data;

import com.loyayz.gaia.data.Sorter;
import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.converter.AppConverter;
import com.loyayz.uaa.data.entity.UaaApp;
import com.loyayz.uaa.data.entity.UaaAppMenu;
import com.loyayz.uaa.data.entity.UaaAppMenuAction;
import com.loyayz.uaa.dto.AppQueryParam;
import com.loyayz.uaa.dto.MenuQueryParam;
import com.loyayz.uaa.dto.SimpleApp;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class AppRepository {

    public static Long insertApp(SimpleApp app) {
        UaaApp entity = AppConverter.toEntity(app);
        entity.insert();
        return entity.getId();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMapper#listByParam}
     */
    public static List<UaaApp> listAppByParam(AppQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaApp.class, "listByParam", queryParam);
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
