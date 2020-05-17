package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class UserRoles extends AbstractEntity<UaaUserRole> {
    private final Long userId;
    private final Set<String> newCodes = new HashSet<>();
    private final Set<String> deletedCodes = new HashSet<>();

    static UserRoles of(Long userId) {
        return new UserRoles(userId);
    }

    void addCodes(List<String> roleCodes) {
        List<String> existRoles = UserRepository.listRoleCodeByUser(this.userId);
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

    @Override
    public void save() {
        this.delete();

        if (this.newCodes.isEmpty()) {
            return;
        }
        List<UaaUserRole> userRoles = this.newCodes.stream()
                .map(roleCode -> new UaaUserRole(this.userId, roleCode))
                .collect(Collectors.toList());
        new UaaUserRole().insert(userRoles);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUserRoles}
     */
    @Override
    public void delete() {
        if (this.deletedCodes.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId);
        param.put("roleCodes", this.deletedCodes);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUserRoles", param);
    }

    @Override
    protected UaaUserRole buildEntity() {
        return null;
    }

    private UserRoles(Long userId) {
        this.userId = userId;
    }

}
