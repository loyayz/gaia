package com.loyayz.uaa.converter;

import com.loyayz.uaa.data.entity.UaaApp;
import com.loyayz.uaa.data.entity.UaaAppMenu;
import com.loyayz.uaa.data.entity.UaaAppMenuAction;
import com.loyayz.uaa.dto.MenuDirectory;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.uaa.dto.SimpleMenuAction;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class AppConverter {

    public static UaaApp toEntity(SimpleApp app) {
        return UaaApp.builder()
                .name(app.getName())
                .remote(app.getRemote() != null && app.getRemote() ? 1 : 0)
                .url(app.getUrl() == null ? "" : app.getUrl())
                .remark(app.getRemark())
                .sort(app.getSort())
                .build();
    }

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

    public static void setEntity(UaaApp entity, SimpleApp app) {
        entity.setName(app.getName());
        entity.setRemote(app.getRemote() != null && app.getRemote() ? 1 : 0);
        entity.setUrl(app.getUrl());
        entity.setRemark(app.getRemark());
        entity.setSort(app.getSort());
    }

    public static SimpleMenu toSimple(UaaAppMenu menu) {
        SimpleMenu result;
        if (menu.getDir() == 1) {
            result = new MenuDirectory();
        } else {
            result = new SimpleMenu();
        }
        result.setParentCode(menu.getParentCode());
        result.setCode(menu.getCode());
        result.setName(menu.getName());
        result.setUrl(menu.getUrl());
        result.setIcon(menu.getIcon());
        result.setRemark(menu.getRemark());
        result.setSort(menu.getSort());
        return result;
    }

    public static UaaAppMenu toEntity(Long appId, MenuDirectory dir) {
        return UaaAppMenu.builder()
                .appId(appId)
                .parentCode(dir.getParentCode())
                .dir(1)
                .code(dir.getCode())
                .name(dir.getName())
                .url("")
                .icon(dir.getIcon() == null ? "" : dir.getIcon())
                .remark(dir.getRemark() == null ? "" : dir.getRemark())
                .sort(dir.getSort())
                .build();
    }

    public static void setEntity(UaaAppMenu menu, Long appId, MenuDirectory dir) {
        menu.setAppId(appId);
        menu.setParentCode(dir.getParentCode());
        menu.setDir(1);
        menu.setCode(dir.getCode());
        menu.setName(dir.getName());
        menu.setIcon(dir.getIcon());
        menu.setRemark(dir.getRemark());
        menu.setSort(dir.getSort());
    }

    public static UaaAppMenu toEntity(Long appId, SimpleMenu menu) {
        return UaaAppMenu.builder()
                .appId(appId)
                .parentCode(menu.getParentCode())
                .dir(0)
                .code(menu.getCode())
                .name(menu.getName())
                .url(menu.getUrl())
                .icon(menu.getIcon() == null ? "" : menu.getIcon())
                .remark(menu.getRemark() == null ? "" : menu.getRemark())
                .sort(menu.getSort())
                .build();
    }

    public static void setEntity(UaaAppMenu entity, Long appId, SimpleMenu menu) {
        entity.setAppId(appId);
        entity.setParentCode(menu.getParentCode());
        entity.setDir(0);
        entity.setCode(menu.getCode());
        entity.setName(menu.getName());
        entity.setUrl(menu.getUrl());
        entity.setIcon(menu.getIcon());
        entity.setRemark(menu.getRemark());
        entity.setSort(menu.getSort());
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
