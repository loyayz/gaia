package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.zeus.EntityId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppHelper {
    private final EntityId appId;
    private final List<String> newRoleNames = new ArrayList<>();
    private final List<AppMenuMeta> newMenuMetas = new ArrayList<>();

    static AppHelper of(EntityId appId) {
        return new AppHelper(appId);
    }

    void addRole(String name) {
        this.newRoleNames.add(name);
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
        this.saveRoles();
        this.saveMenus();
    }

    private void saveRoles() {
        if (this.newRoleNames.isEmpty()) {
            return;
        }
        List<UaaRole> roles = this.newRoleNames.stream()
                .map(name -> new UaaRole(this.appId.get(), name))
                .collect(Collectors.toList());
        new UaaRole().insert(roles);
        this.newRoleNames.clear();
    }

    private void saveMenus() {
        this.newMenuMetas.forEach(AppMenuMeta::save);
        this.newMenuMetas.clear();
    }

    private AppHelper(EntityId appId) {
        this.appId = appId;
    }
}
