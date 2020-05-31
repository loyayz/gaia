package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.data.UaaAppRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppRoles {
    private final AppId appId;

    private final List<String> newRoles = new ArrayList<>();
    private final Map<Long, AppRole> deletedRoles = new HashMap<>();

    static AppRoles of(AppId appId) {
        return new AppRoles(appId);
    }

    void addRole(List<String> names) {
        this.newRoles.addAll(names);
    }

    void removeRole(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            this.deletedRoles.put(roleId, AppRole.of(roleId));
        }
    }

    void save() {
        Long appId = this.appId.get();
        if (!this.newRoles.isEmpty()) {
            new UaaAppRole().insert(
                    this.newRoles.stream()
                            .map(name -> UaaAppRole.builder().appId(appId).name(name).build())
                            .collect(Collectors.toList())
            );
        }

        this.deletedRoles.forEach((roleId, role) -> role.delete());
    }

    private AppRoles(AppId appId) {
        this.appId = appId;
    }

}
