package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class RoleUsers {
    private final RoleId roleId;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static RoleUsers of(RoleId roleId) {
        return new RoleUsers(roleId);
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
    }

    private void insert() {
        if (this.newUsers.isEmpty()) {
            return;
        }
        List<Long> existUsers = this.roleId.isEmpty() ? Collections.emptyList() : UserRepository.listUserIdByRole(this.roleId.get());
        List<UaaUserRole> userRoles = this.newUsers.stream()
                .filter(userId -> !existUsers.contains(userId))
                .map(userId -> new UaaUserRole(userId, this.roleId.get()))
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
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleId", this.roleId.get());
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUsersRole", param);
    }

    private RoleUsers(RoleId roleId) {
        this.roleId = roleId;
    }

}
