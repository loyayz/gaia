package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.common.dto.SimpleMenuAction;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.zeus.AbstractEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppMenus {
    private final AppId appId;

    private final Map<String, AppMenuMeta> menuMetas = new HashMap<>(16);
    private final List<String> deletedMenuCodes = new ArrayList<>();

    private List<AppMenuAction> menuActions = new ArrayList<>();
    private final Map<String, List<String>> deletedMenuActions = new HashMap<>();

    static AppMenus of(AppId appId) {
        return new AppMenus(appId);
    }

    void addMeta(String parentCode, List<SimpleMenu> menus) {
        for (SimpleMenu menu : menus) {
            this.addMeta(menu.getCode(), menu.getItems());

            menu.setParentCode(parentCode);
            this.buildMenuMeta(menu).ifPresent(m -> this.menuMetas.put(menu.getCode(), m));
        }
    }

    void removeMeta(List<String> menuCodes) {
        for (String menuCode : menuCodes) {
            this.menuMetas.remove(menuCode);
        }
        this.deletedMenuCodes.addAll(menuCodes);
    }

    void addAction(String menuCode, List<SimpleMenuAction> actions) {
        this.menuActions.addAll(
                actions.stream()
                        .map(action -> AppMenuAction.of(this.appId, menuCode, action))
                        .collect(Collectors.toList())
        );
    }

    void removeAction(String menuCode, List<String> actionCodes) {
        this.menuActions = this.menuActions.stream()
                .filter(action -> !(menuCode.equals(action.menuCode()) && actionCodes.contains(action.actionCode())))
                .collect(Collectors.toList());
        List<String> existCodes = this.deletedMenuActions.getOrDefault(menuCode, new ArrayList<>());
        existCodes.addAll(actionCodes);
        this.deletedMenuActions.put(menuCode, existCodes);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        this.menuMetas.forEach((menuCode, menu) -> menu.save());
        this.menuActions.forEach(AbstractEntity::save);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMetaMapper#deleteByCodes}
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenus}
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#deleteByCodes}
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenuAndCodes}
     */
    private void delete() {
        if (!this.deletedMenuCodes.isEmpty()) {
            Map<String, Object> param = new HashMap<>(2);
            param.put("menuCodes", this.deletedMenuCodes);
            MybatisUtils.executeDelete(UaaAppMenuMeta.class, "deleteByCodes", param);
            MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenus", param);
            MybatisUtils.executeDelete(UaaMenu.class, "deleteByCodes", param);
        }
        this.deletedMenuActions.forEach((menuCode, actionCodes) -> {
            Map<String, Object> param = new HashMap<>(3);
            param.put("menuCode", menuCode);
            param.put("actionCodes", actionCodes);
            MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenuAndCodes", param);
        });
    }

    private Optional<AppMenuMeta> buildMenuMeta(SimpleMenu menu) {
        String menuCode = menu.getCode();
        if (menuCode == null || menuCode.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(AppMenuMeta.of(this.appId, menu));
    }

    private AppMenus(AppId appId) {
        this.appId = appId;
    }
}
