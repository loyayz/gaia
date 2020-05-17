package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class RoleUsers extends AbstractEntity<UaaUserRole> {
    private final String roleCode;
    private final Set<Long> newUsers = new HashSet<>();
    private final Set<Long> deletedUsers = new HashSet<>();

    static RoleUsers of(String roleCode) {
        return new RoleUsers(roleCode);
    }

    void addUsers(List<Long> userIds) {
        List<Long> existUsers = UserRepository.listUserIdByRole(this.roleCode);
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

    @Override
    public void save() {
        this.delete();

        if (this.newUsers.isEmpty()) {
            return;
        }
        List<UaaUserRole> userRoles = this.newUsers.stream()
                .map(userId -> new UaaUserRole(userId, this.roleCode))
                .collect(Collectors.toList());
        new UaaUserRole().insert(userRoles);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUsersRole}
     */
    @Override
    public void delete() {
        if (this.deletedUsers.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleCode", this.roleCode);
        param.put("userIds", this.deletedUsers);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUsersRole", param);
    }

    @Override
    protected UaaUserRole buildEntity() {
        return null;
    }

    private RoleUsers(String roleCode) {
        this.roleCode = roleCode;
    }

}
