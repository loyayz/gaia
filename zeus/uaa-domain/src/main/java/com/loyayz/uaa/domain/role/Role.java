package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Role extends AbstractEntity<UaaRole> {
    private final RoleId roleId;
    private RoleUsers roleUsers;
    private RolePermissions rolePermissions;

    public static Role of(Long id) {
        return new Role(id);
    }

    public Long id() {
        return this.roleId.get();
    }

    public Role name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    /**
     * 添加用户
     *
     * @param userIds 用户id
     */
    public Role addUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = RoleUsers.of(this.roleId);
        }
        this.roleUsers.addUsers(userIds);
        return this;
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     */
    public Role removeUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = RoleUsers.of(this.roleId);
        }
        this.roleUsers.removeUsers(userIds);
        return this;
    }

    public Role addPermission(BasePermission permission) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleId);
        }
        this.rolePermissions.add(permission);
        return this;
    }

    public Role removePermission(BasePermission permission) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleId);
        }
        this.rolePermissions.remove(permission);
        return this;
    }

    @Override
    protected UaaRole buildEntity() {
        return RoleRepository.getRole(this.roleId.get());
    }

    @Override
    public void save() {
        super.save();

        if (this.roleUsers != null) {
            this.roleUsers.save();
        }
        if (this.rolePermissions != null) {
            this.rolePermissions.save();
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRole}
     * {@link com.loyayz.uaa.data.mapper.UaaRolePermissionMapper#deleteByRole}
     */
    @Override
    public void delete() {
        new UaaRole().deleteById(this.roleId.get());

        Map<String, Object> param = new HashMap<>(2);
        param.put("roleId", this.roleId.get());
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
        // delete rolePermission
        MybatisUtils.executeDelete(UaaRolePermission.class, "deleteByRole", param);
    }

    private Role(Long roleId) {
        this.roleId = RoleId.of(roleId);
    }
}
