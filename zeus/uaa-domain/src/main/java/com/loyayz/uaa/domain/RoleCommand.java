package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.api.Role;
import com.loyayz.uaa.data.UserRepository;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.data.entity.UaaRole;
import com.loyayz.uaa.data.entity.UaaUserRole;
import com.loyayz.uaa.dto.SimpleRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class RoleCommand implements Role {
    private final String roleCode;

    private RoleCommand(String roleCode) {
        this.roleCode = roleCode;
    }

    public static Role getInstance(String roleCode) {
        return new RoleCommand(roleCode);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#deleteByCode}
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRole}
     */
    @Override
    public void delete() {
        // delete role
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleCode", this.roleCode);

        MybatisUtils.executeDelete(UaaRole.class, "deleteByCode", param);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#updateByCode}
     */
    @Override
    public void update(SimpleRole role) {
        UaaRole entity = RoleConverter.toEntity(role);
        MybatisUtils.executeUpdate(UaaRole.class, "updateByCode", entity);
    }

    @Override
    public void addUser(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return;
        }
        List<Long> existUserIds = UserRepository.listUserByRole(roleCode);
        List<UaaUserRole> roles = userIds.stream()
                .filter(userId -> !existUserIds.contains(userId))
                .map(userId -> new UaaUserRole(userId, this.roleCode))
                .collect(Collectors.toList());
        new UaaUserRole().insert(roles);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteRoleByUsers}
     */
    @Override
    public void deleteUser(List<Long> userIds) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("userIds", userIds);
        param.put("roleCode", this.roleCode);
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteRoleByUsers", param);
    }
}
