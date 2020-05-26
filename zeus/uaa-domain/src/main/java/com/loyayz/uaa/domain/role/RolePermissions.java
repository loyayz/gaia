package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRoleAction;
import com.loyayz.uaa.data.UaaRoleApp;
import com.loyayz.uaa.data.UaaRoleMenu;
import com.loyayz.uaa.domain.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class RolePermissions {
    private final RoleId roleCode;
    private final Set<Long> newApps = new HashSet<>();
    private final Set<Long> deletedApps = new HashSet<>();
    private final Set<Long> newMenus = new HashSet<>();
    private final Set<Long> deletedMenus = new HashSet<>();
    private final Set<Long> newActions = new HashSet<>();
    private final Set<Long> deletedActions = new HashSet<>();

    static RolePermissions of(RoleId roleCode) {
        return new RolePermissions(roleCode);
    }

    void addApps(List<Long> appIds) {
        List<Long> existApps = this.roleCode.isEmpty() ? Collections.emptyList() : RoleRepository.listAppIdByRole(this.roleCode.get());
        for (Long appId : appIds) {
            if (!existApps.contains(appId)) {
                this.newApps.add(appId);
            }
        }
    }

    void removeApps(List<Long> appIds) {
        this.newApps.removeAll(appIds);
        this.deletedApps.addAll(appIds);
    }

    void addMenus(List<Long> menuIds) {
        List<Long> existMenus = this.roleCode.isEmpty() ? Collections.emptyList() : RoleRepository.listMenuIdByRole(this.roleCode.get());
        for (Long menuId : menuIds) {
            if (!existMenus.contains(menuId)) {
                this.newMenus.add(menuId);
            }
        }
    }

    void removeMenus(List<Long> menuIds) {
        this.newMenus.removeAll(menuIds);
        this.deletedMenus.addAll(menuIds);
    }

    void addActions(List<Long> actionIds) {
        List<Long> existActions = this.roleCode.isEmpty() ? Collections.emptyList() : RoleRepository.listActionIdByRole(this.roleCode.get());
        for (Long actionId : actionIds) {
            if (!existActions.contains(actionId)) {
                this.newActions.add(actionId);
            }
        }
    }

    void removeActions(List<Long> actionIds) {
        this.newActions.removeAll(actionIds);
        this.deletedActions.addAll(actionIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        List<UaaRoleApp> roleApps = this.newApps.stream()
                .map(appId -> new UaaRoleApp(this.roleCode.get(), appId))
                .collect(Collectors.toList());
        List<UaaRoleMenu> roleMenus = this.newMenus.stream()
                .map(menuId -> new UaaRoleMenu(this.roleCode.get(), menuId))
                .collect(Collectors.toList());
        List<UaaRoleAction> roleActions = this.newActions.stream()
                .map(actionId -> new UaaRoleAction(this.roleCode.get(), actionId))
                .collect(Collectors.toList());

        if (!roleApps.isEmpty()) {
            new UaaRoleApp().insert(roleApps);
        }
        if (!roleMenus.isEmpty()) {
            new UaaRoleMenu().insert(roleMenus);
        }
        if (!roleActions.isEmpty()) {
            new UaaRoleAction().insert(roleActions);
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleAppMapper#deleteByRoleApps}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMenuMapper#deleteByRoleMenus}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleActionMapper#deleteByRoleActions}
     */
    private void delete() {
        Map<String, Object> param = new HashMap<>(3);

        if (!this.deletedApps.isEmpty()) {
            param.put("roleCode", this.roleCode.get());
            param.put("appIds", this.deletedApps);
            MybatisUtils.executeDelete(UaaRoleApp.class, "deleteByRoleApps", param);
        }
        if (!this.deletedMenus.isEmpty()) {
            param.put("menuIds", this.deletedMenus);
            MybatisUtils.executeDelete(UaaRoleMenu.class, "deleteByRoleMenus", param);
        }
        if (!this.deletedActions.isEmpty()) {
            param.put("actionIds", this.deletedActions);
            MybatisUtils.executeDelete(UaaRoleAction.class, "deleteByRoleActions", param);
        }
    }

    private RolePermissions(RoleId roleCode) {
        this.roleCode = roleCode;
    }

}
