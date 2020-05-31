package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppRoleUsers {
    private final Long roleId;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static AppRoleUsers of(Long roleId) {
        return new AppRoleUsers(roleId);
    }

    void addUsers(List<Long> userIds) {
        this.newUsers.addAll(userIds);
    }

    void removeUsers(List<Long> userIds) {
        this.newUsers.removeAll(userIds);
        this.deletedUsers.addAll(userIds);
    }

    void save() {
        this.insert();
        this.delete();

        this.newUsers.clear();
        this.deletedUsers.clear();
    }

    private void insert() {
        if (this.newUsers.isEmpty()) {
            return;
        }
        List<Long> existUsers = UserRepository.listUserIdByRole(this.roleId);
        List<UaaUserRole> userRoles = this.newUsers.stream()
                .filter(userId -> !existUsers.contains(userId))
                .map(userId -> new UaaUserRole(userId, this.roleId))
                .collect(Collectors.toList());
        if (!userRoles.isEmpty()) {
            new UaaUserRole().insert(userRoles);
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUsersRole}
     */
    private void delete() {
        if (this.deletedUsers.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("roleId", this.roleId);
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUsersRole", param);
    }

    private AppRoleUsers(Long roleId) {
        this.roleId = roleId;
    }

}
