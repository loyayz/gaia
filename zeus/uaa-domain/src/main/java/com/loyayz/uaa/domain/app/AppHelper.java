package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.data.UaaAppRole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppHelper {
    private final AppId appId;
    private final List<String> newRoleNames = new ArrayList<>();
    private final List<AppMenuMeta> newMenuMetas = new ArrayList<>();

    static AppHelper of(AppId appId) {
        return new AppHelper(appId);
    }

    void addRole(List<String> names) {
        this.newRoleNames.addAll(names);
    }

    void addMenu(Long pid, List<SimpleMenu> menus) {
        if (pid == null) {
            pid = ROOT_MENU_CODE;
        }
        for (SimpleMenu menu : menus) {
            menu.setPid(pid);
            menu.setId(null);

            AppMenuMeta menuMeta = AppMenuMeta.of(this.appId).info(menu);
            this.newMenuMetas.add(menuMeta);
            this.addMenu(menuMeta.id(), menu.getItems());
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
        List<UaaAppRole> roles = this.newRoleNames.stream()
                .map(name -> new UaaAppRole(this.appId.get(), name))
                .collect(Collectors.toList());
        new UaaAppRole().insert(roles);
        this.newRoleNames.clear();
    }

    private void saveMenus() {
        this.newMenuMetas.forEach(AppMenuMeta::save);
        this.newMenuMetas.clear();
    }

    private AppHelper(AppId appId) {
        this.appId = appId;
    }
}
