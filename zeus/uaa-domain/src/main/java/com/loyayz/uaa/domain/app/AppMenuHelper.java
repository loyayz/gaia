package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.dto.SimpleMenu;

import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppMenuHelper {
    private final AppId appId;
    private final Map<String, AppMenuMeta> menuMetas = new HashMap<>(16);
    private final Set<String> deletedCodes = new HashSet<>();

    static AppMenuHelper of(AppId appId) {
        return new AppMenuHelper(appId);
    }

    void removeCodes(List<String> menuCodes) {
        for (String menuCode : menuCodes) {
            this.menuMetas.remove(menuCode);
        }
        this.deletedCodes.addAll(menuCodes);
    }

    void addMeta(String parentCode, List<SimpleMenu> menus) {
        for (SimpleMenu menu : menus) {
            this.addMeta(menu.getCode(), menu.getItems());

            menu.setParentCode(parentCode);
            this.buildMenuMeta(menu).ifPresent(m -> this.menuMetas.put(menu.getCode(), m));
        }
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        this.menuMetas.forEach((menuCode, menu) -> menu.save());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMetaMapper#deleteByCodes}
     */
    private void delete() {
        if (this.deletedCodes.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("codes", this.deletedCodes);
        MybatisUtils.executeDelete(UaaAppMenuMeta.class, "deleteByCodes", param);
    }

    private Optional<AppMenuMeta> buildMenuMeta(SimpleMenu menu) {
        String menuCode = menu.getCode();
        if (menuCode == null || menuCode.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(AppMenuMeta.of(this.appId, menu));
    }

    private AppMenuHelper(AppId appId) {
        this.appId = appId;
    }
}
