package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRoleApp;
import com.loyayz.uaa.data.UaaUserRole;
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

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newApps.isEmpty()) {
            return;
        }
        List<UaaRoleApp> roleApps = this.newApps.stream()
                .map(appId -> new UaaRoleApp(this.roleCode.get(),appId))
                .collect(Collectors.toList());
        new UaaRoleApp().insert(roleApps);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleAppMapper#deleteByRoleApps}
     */
    private void delete() {
        if (this.deletedApps.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("roleCode", this.roleCode.get());
        param.put("appIds", this.deletedApps);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRoleApps", param);
    }

    private RolePermissions(RoleId roleCode) {
        this.roleCode = roleCode;
    }

}
