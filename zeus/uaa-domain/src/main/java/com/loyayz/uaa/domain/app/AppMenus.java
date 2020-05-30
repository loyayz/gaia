package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.common.dto.SimpleMenuAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppMenus {
    private final AppId appId;

    private final Map<Long, AppMenuMeta> menuMetas = new HashMap<>();
    private final Map<Long, AppMenuMeta> deletedMenuMetas = new HashMap<>();

    static AppMenus of(AppId appId) {
        return new AppMenus(appId);
    }

    void addMeta(Long pid, List<SimpleMenu> menus) {
        for (SimpleMenu menu : menus) {
            menu.setPid(pid);

            AppMenuMeta menuMeta = AppMenuMeta.of(this.appId, menu);
            if (menu.getId() != null) {
                menuMeta = this.menuMetas.getOrDefault(menu.getId(), menuMeta);
                menuMeta.info(menu);
            }
            Long menuId = menuMeta.id();
            this.menuMetas.put(menuId, menuMeta);
            this.addMeta(menuId, menu.getItems());
        }
    }

    void removeMeta(List<Long> menuIds) {
        for (Long menuId : menuIds) {
            this.menuMetas.remove(menuId);
            this.deletedMenuMetas.put(menuId, AppMenuMeta.of(this.appId, menuId));
        }
    }

    void addAction(Long menuId, List<SimpleMenuAction> actions) {
        AppMenuMeta menuMeta = this.menuMetas.getOrDefault(menuId, AppMenuMeta.of(this.appId, menuId));
        menuMeta.addAction(actions);
        this.menuMetas.put(menuId, menuMeta);
    }

    void removeAction(Long menuId, List<String> actionCodes) {
        AppMenuMeta menuMeta = this.menuMetas.getOrDefault(menuId, AppMenuMeta.of(this.appId, menuId));
        menuMeta.removeAction(actionCodes);
        this.menuMetas.put(menuId, menuMeta);
    }

    void save() {
        this.menuMetas.forEach((menuId, menu) -> menu.save());
        this.deletedMenuMetas.forEach((menuId, menu) -> menu.delete());
    }

    private AppMenus(AppId appId) {
        this.appId = appId;
    }
}
