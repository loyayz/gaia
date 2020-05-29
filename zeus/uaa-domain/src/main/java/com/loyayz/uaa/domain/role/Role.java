package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.constant.RolePermissionType;
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

    public static Role of() {
        return of(null);
    }

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
    public Role addUser(Long... userIds) {
        return this.addUser(Arrays.asList(userIds));
    }

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
    public Role removeUser(Long... userIds) {
        return this.removeUser(Arrays.asList(userIds));
    }

    public Role removeUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = RoleUsers.of(this.roleId);
        }
        this.roleUsers.removeUsers(userIds);
        return this;
    }

    /**
     * 添加应用权限
     *
     * @param appIds 应用id
     */
    public Role addAppPermission(Long... appIds) {
        return this.addAppPermission(Arrays.asList(appIds));
    }

    public Role addAppPermission(List<Long> appIds) {
        return this.addPermission(RolePermissionType.APP, appIds);
    }

    /**
     * 删除应用权限
     *
     * @param appIds 应用id
     */
    public Role removeAppPermission(Long... appIds) {
        return this.removeAppPermission(Arrays.asList(appIds));
    }

    public Role removeAppPermission(List<Long> appIds) {
        return this.removePermission(RolePermissionType.APP, appIds);
    }

    /**
     * 添加菜单权限
     *
     * @param menuIds 菜单id
     */
    public Role addMenuPermission(Long... menuIds) {
        return this.addMenuPermission(Arrays.asList(menuIds));
    }

    public Role addMenuPermission(List<Long> menuIds) {
        return this.addPermission(RolePermissionType.MENU, menuIds);
    }

    /**
     * 删除菜单权限
     *
     * @param menuIds 菜单id
     */
    public Role removeMenuPermission(Long... menuIds) {
        return this.removeMenuPermission(Arrays.asList(menuIds));
    }

    public Role removeMenuPermission(List<Long> menuIds) {
        return this.removePermission(RolePermissionType.MENU, menuIds);
    }

    /**
     * 添加功能权限
     *
     * @param actionIds 应用id
     */
    public Role addActionPermission(Long... actionIds) {
        return this.addActionPermission(Arrays.asList(actionIds));
    }

    public Role addActionPermission(List<Long> actionIds) {
        return this.addPermission(RolePermissionType.ACTION, actionIds);
    }

    /**
     * 删除功能权限
     *
     * @param actionIds 应用id
     */
    public Role removeActionPermission(Long... actionIds) {
        return this.removeActionPermission(Arrays.asList(actionIds));
    }

    public Role removeActionPermission(List<Long> actionIds) {
        return this.removePermission(RolePermissionType.ACTION, actionIds);
    }

    private Role addPermission(RolePermissionType type, List<Long> refIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleId);
        }
        this.rolePermissions.addPermission(type, refIds);
        return this;
    }

    private Role removePermission(RolePermissionType type, List<Long> refIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleId);
        }
        this.rolePermissions.removePermission(type, refIds);
        return this;
    }

    @Override
    protected UaaRole buildEntity() {
        if (this.roleId.isEmpty()) {
            return new UaaRole();
        }
        UaaRole entity = RoleRepository.getRole(this.roleId.get());
        if (entity == null) {
            entity = new UaaRole();
        }
        return entity;
    }

    @Override
    public void save() {
        super.save();
        this.roleId.set(super.entity().getId());

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
        super.delete();

        Map<String, Object> param = new HashMap<>(2);
        param.put("roleId", this.roleId.get());
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
        // delete rolePermission
        MybatisUtils.executeDelete(UaaRolePermission.class, "deleteByRole", param);
    }

    private Role(Long id) {
        this.roleId = RoleId.of(id);
    }

}
