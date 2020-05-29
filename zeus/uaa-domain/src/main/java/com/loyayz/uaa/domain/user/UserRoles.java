package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class UserRoles {
    private final UserId userId;
    private final Set<Long> newRoles = new HashSet<>();
    private final Set<Long> deletedRoles = new HashSet<>();

    static UserRoles of(UserId userId) {
        return new UserRoles(userId);
    }

    void addRoles(List<Long> roleIds) {
        this.newRoles.addAll(roleIds);
    }

    void removeRoles(List<Long> roleIds) {
        this.newRoles.removeAll(roleIds);
        this.deletedRoles.addAll(roleIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newRoles.isEmpty()) {
            return;
        }
        List<Long> existRoles = this.userId.isEmpty() ? Collections.emptyList() : UserRepository.listRoleIdByUser(this.userId.get());
        List<UaaUserRole> userRoles = this.newRoles.stream()
                .filter(roleId -> !existRoles.contains(roleId))
                .map(roleId -> new UaaUserRole(this.userId.get(), roleId))
                .collect(Collectors.toList());
        if (!userRoles.isEmpty()) {
            new UaaUserRole().insert(userRoles);
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUserRoles}
     */
    private void delete() {
        if (this.deletedRoles.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId.get());
        param.put("roleIds", this.deletedRoles);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUserRoles", param);
    }

    private UserRoles(UserId userId) {
        this.userId = userId;
    }

}
