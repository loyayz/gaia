package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.zeus.EntityId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class RoleUsers {
    private final EntityId roleId;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static RoleUsers of(EntityId roleId) {
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
        Long rid = this.roleId.get();
        List<Long> existUsers = RoleRepository.listUserIdByRoleUsers(rid, new ArrayList<>(this.newUsers));
        this.newUsers.removeAll(existUsers);

        List<UaaUserRole> userRoles = this.newUsers.stream()
                .map(userId -> new UaaUserRole(userId, rid))
                .collect(Collectors.toList());
        if (!userRoles.isEmpty()) {
            new UaaUserRole().insert(userRoles);
        }
        this.newUsers.clear();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRoleUsers}
     */
    private void delete() {
        if (this.deletedUsers.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("roleId", this.roleId.get());
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRoleUsers", param);
        this.deletedUsers.clear();
    }

    private RoleUsers(EntityId roleId) {
        this.roleId = roleId;
    }

}
