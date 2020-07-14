package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.mapper.UaaAppMenuMetaMapper;
import com.loyayz.uaa.dto.AppQueryParam;
import com.loyayz.uaa.dto.MenuQueryParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMapper#listByUser}
     */
    public static List<UaaApp> listAppByUser(Long userId) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", userId);
        return MybatisUtils.executeSelectList(UaaApp.class, "listByUser", param);
    }

    public static UaaAppMenuMeta getAppMenu(Long menuId) {
        return new UaaAppMenuMeta().findById(menuId);
    }

    /**
     * {@link UaaAppMenuMetaMapper#listByParam}
     */
    public static List<UaaAppMenuMeta> listAppMenuByParam(MenuQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaAppMenuMeta.class, "listByParam", queryParam);
    }

    public static Integer countAppMenuByParent(Long pid) {
        return UaaAppMenuMeta.builder().pid(pid).build()
                .countByCondition();
    }

    public static UaaAppMenuAction getAppMenuActionByCode(Long menuId, String code) {
        return UaaAppMenuAction.builder().menuMetaId(menuId).code(code).build()
                .listByCondition()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public static List<UaaAppMenuAction> listAppMenuActionByMenu(Long menuId) {
        return UaaAppMenuAction.builder().menuMetaId(menuId).build()
                .listByCondition();
    }

}
