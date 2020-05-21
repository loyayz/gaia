package com.loyayz.uaa.data.converter;

import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.uaa.dto.SimpleMenuAction;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class AppConverter {

    public static SimpleApp toSimple(UaaApp app) {
        SimpleApp result = new SimpleApp();
        result.setId(app.getId());
        result.setName(app.getName());
        result.setRemote(app.getRemote() == 1);
        result.setUrl(app.getUrl());
        result.setRemark(app.getRemark());
        result.setSort(app.getSort());
        return result;
    }

    public static SimpleMenu toSimple(UaaAppMenuMeta menu) {
        SimpleMenu result = new SimpleMenu();
        result.setParentCode(menu.getParentCode());
        result.setCode(menu.getCode());
        result.setName(menu.getName());
        result.setUrl(menu.getUrl());
        result.setIcon(menu.getIcon());
        result.setSort(menu.getSort());
        result.setDir(menu.getDir() == 1);
        return result;
    }

    public static UaaAppMenuMeta toEntity(Long appId, SimpleMenu menu) {
        return UaaAppMenuMeta.builder()
                .appId(appId)
                .parentCode(menu.getParentCode())
                .dir(menu.getDir() ? 1 : 0)
                .code(menu.getCode())
                .name(menu.getName())
                .url(menu.getUrl() == null ? "" : menu.getUrl())
                .icon(menu.getIcon() == null ? "" : menu.getIcon())
                .sort(menu.getSort())
                .build();
    }

    public static void setEntity(UaaAppMenuMeta entity, Long appId, SimpleMenu menu) {
        entity.setAppId(appId);
        entity.setParentCode(menu.getParentCode());
        entity.setName(menu.getName());
        entity.setUrl(menu.getUrl());
        entity.setIcon(menu.getIcon());
        entity.setSort(menu.getSort());
        // 菜单可修改为目录，目录无法修改为菜单
        if (menu.getDir()) {
            entity.setDir(1);
        }
    }

    public static UaaAppMenuAction toEntity(Long appId, SimpleMenuAction action) {
        return UaaAppMenuAction.builder()
                .appId(appId)
                .menuCode(action.getMenuCode())
                .code(action.getCode())
                .name(action.getName())
                .remark(action.getRemark())
                .sort(action.getSort())
                .build();
    }

    public static SimpleMenuAction toSimple(UaaAppMenuAction action) {
        SimpleMenuAction result = new SimpleMenuAction();
        result.setMenuCode(action.getMenuCode());
        result.setCode(action.getCode());
        result.setName(action.getName());
        result.setRemark(action.getRemark());
        result.setSort(action.getSort());
        return result;
    }

}
