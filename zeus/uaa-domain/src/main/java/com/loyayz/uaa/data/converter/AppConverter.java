package com.loyayz.uaa.data.converter;

import com.loyayz.gaia.util.Functions;
import com.loyayz.uaa.common.dto.SimpleApp;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.common.dto.SimpleMenuAction;
import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class AppConverter {

    public static SimpleApp toSimple(UaaApp app) {
        SimpleApp result = new SimpleApp();
        result.setId(app.getId());
        result.setName(app.getName());
        result.setUrl(app.getUrl());
        result.setRemark(app.getRemark());
        result.setSort(app.getSort());
        return result;
    }

    public static SimpleMenu toSimple(UaaAppMenuMeta menu) {
        SimpleMenu result = new SimpleMenu();
        result.setPid(menu.getPid());
        result.setCode(menu.getCode());
        result.setName(menu.getName());
        result.setUrl(menu.getUrl());
        result.setIcon(menu.getIcon());
        result.setSort(menu.getSort());
        result.setDir(menu.getDir() == 1);
        return result;
    }

    public static UaaAppMenuMeta toEntity(Long menuId, SimpleMenu menu) {
        UaaAppMenuMeta entity = UaaAppMenuMeta.builder()
                .pid(menu.getPid())
                .dir(menu.getDir() ? 1 : 0)
                .code(menu.getCode() == null ? "" : menu.getCode())
                .name(menu.getName())
                .url(menu.getUrl() == null ? "" : menu.getUrl())
                .icon(menu.getIcon() == null ? "" : menu.getIcon())
                .sort(menu.getSort() == null ? 0 : menu.getSort())
                .build();
        entity.setId(menuId);
        return entity;
    }

    public static void setEntity(UaaAppMenuMeta entity, SimpleMenu menu) {
        Functions.executeIfNotNull(entity::setPid, menu.getPid());
        Functions.executeIfNotNull(entity::setName, menu.getName());
        Functions.executeIfNotNull(entity::setUrl, menu.getUrl());
        Functions.executeIfNotNull(entity::setIcon, menu.getIcon());
        Functions.executeIfNotNull(entity::setSort, menu.getSort());
        // 菜单可修改为目录，目录无法修改为菜单
        if (menu.getDir()) {
            entity.setDir(1);
        }
    }

    public static UaaAppMenuAction toEntity(Long menuId, String code) {
        return UaaAppMenuAction.builder()
                .menuMetaId(menuId)
                .code(code)
                .build();
    }

    public static SimpleMenuAction toSimple(UaaAppMenuAction action) {
        SimpleMenuAction result = new SimpleMenuAction();
        result.setCode(action.getCode());
        result.setName(action.getName());
        return result;
    }

}
