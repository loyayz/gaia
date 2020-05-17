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
    private final RoleId roleCode;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static RoleUsers of(RoleId roleCode) {
        return new RoleUsers(roleCode);
    }

    void addUsers(List<Long> userIds) {
        List<Long> existUsers = this.roleCode.isEmpty() ? Collections.emptyList() : UserRepository.listUserIdByRole(this.roleCode.get());
        for (Long userId : userIds) {
            if (!existUsers.contains(userId)) {
                this.newUsers.add(userId);
            }
        }
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
        List<UaaUserRole> userRoles = this.newUsers.stream()
                .map(userId -> new UaaUserRole(userId, this.roleCode.get()))
                .collect(Collectors.toList());
        new UaaUserRole().insert(userRoles);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUsersRole}
     */
    private void delete() {
        if (this.deletedUsers.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleCode", this.roleCode.get());
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUsersRole", param);
    }

    private RoleUsers(RoleId roleCode) {
        this.roleCode = roleCode;
    }

}
