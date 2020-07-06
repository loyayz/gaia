package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.zeus.EntityId;

import java.util.ArrayList;
import java.util.List;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppHelper {
    private final EntityId appId;
    private final List<AppMenuMeta> newMenuMetas = new ArrayList<>();

    static AppHelper of(EntityId appId) {
        return new AppHelper(appId);
    }

    void addMenu(Long pid, SimpleMenu menu) {
        if (pid == null) {
            pid = ROOT_MENU_CODE;
        }

        menu.setPid(pid);
        menu.setId(null);

        AppMenuMeta menuMeta = AppMenuMeta.of(this.appId).info(menu);
        this.newMenuMetas.add(menuMeta);

        for (SimpleMenu subMenu : menu.getItems()) {
            this.addMenu(menuMeta.idValue(), subMenu);
        }
    }

    void save() {
        this.saveMenus();
    }

    private void saveMenus() {
        this.newMenuMetas.forEach(AppMenuMeta::save);
        this.newMenuMetas.clear();
    }

    private AppHelper(EntityId appId) {
        this.appId = appId;
    }
}
