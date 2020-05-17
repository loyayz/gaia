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
    private final Set<String> newCodes = new HashSet<>();
    private final Set<String> deletedCodes = new HashSet<>();

    static UserRoles of(UserId userId) {
        return new UserRoles(userId);
    }

    void addCodes(List<String> roleCodes) {
        List<String> existRoles = this.userId.isEmpty() ? Collections.emptyList() : UserRepository.listRoleCodeByUser(this.userId.get());
        for (String roleCode : roleCodes) {
            if (!existRoles.contains(roleCode)) {
                this.newCodes.add(roleCode);
            }
        }
    }

    void removeCodes(List<String> roleCodes) {
        this.newCodes.removeAll(roleCodes);
        this.deletedCodes.addAll(roleCodes);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newCodes.isEmpty()) {
            return;
        }
        List<UaaUserRole> userRoles = this.newCodes.stream()
                .map(roleCode -> new UaaUserRole(this.userId.get(), roleCode))
                .collect(Collectors.toList());
        new UaaUserRole().insert(userRoles);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUserRoles}
     */
    private void delete() {
        if (this.deletedCodes.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId.get());
        param.put("roleCodes", this.deletedCodes);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUserRoles", param);
    }

    private UserRoles(UserId userId) {
        this.userId = userId;
    }

}
