package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.*;
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
    private final RoleId roleCode;
    private String name;
    private RoleUsers roleUsers;
    private RolePermissions rolePermissions;

    public static Role of(String roleCode) {
        return new Role(roleCode);
    }

    public String id() {
        return this.roleCode.get();
    }

    public Role name(String name) {
        super.entity().setName(name);
        this.name = name;
        super.markUpdated();
        return this;
    }

    /**
     * 添加用户
     *
     * @param userIds 用户id
     */
    public Role addUser(Long... userIds) {
        this.addUser(Arrays.asList(userIds));
        return this;
    }

    public Role addUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = RoleUsers.of(this.roleCode);
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
        this.removeUser(Arrays.asList(userIds));
        return this;
    }

    public Role removeUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = RoleUsers.of(this.roleCode);
        }
        this.roleUsers.removeUsers(userIds);
        return this;
    }

    /**
     * 添加应用权限
     *
     * @param appIds 应用id
     */
    public Role addApp(Long... appIds) {
        this.addApp(Arrays.asList(appIds));
        return this;
    }

    public Role addApp(List<Long> appIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleCode);
        }
        this.rolePermissions.addApps(appIds);
        return this;
    }

    /**
     * 删除应用权限
     *
     * @param appIds 应用id
     */
    public Role removeApp(Long... appIds) {
        this.removeApp(Arrays.asList(appIds));
        return this;
    }

    public Role removeApp(List<Long> appIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleCode);
        }
        this.rolePermissions.removeApps(appIds);
        return this;
    }

    /**
     * 添加菜单权限
     *
     * @param menuIds 菜单id
     */
    public Role addMenu(Long... menuIds) {
        this.addMenu(Arrays.asList(menuIds));
        return this;
    }

    public Role addMenu(List<Long> menuIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleCode);
        }
        this.rolePermissions.addMenus(menuIds);
        return this;
    }

    /**
     * 删除菜单权限
     *
     * @param menuIds 菜单id
     */
    public Role removeMenu(Long... menuIds) {
        this.removeMenu(Arrays.asList(menuIds));
        return this;
    }

    public Role removeMenu(List<Long> menuIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleCode);
        }
        this.rolePermissions.removeMenus(menuIds);
        return this;
    }

    /**
     * 添加功能权限
     *
     * @param actionIds 应用id
     */
    public Role addAction(Long... actionIds) {
        this.addAction(Arrays.asList(actionIds));
        return this;
    }

    public Role addAction(List<Long> actionIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleCode);
        }
        this.rolePermissions.addActions(actionIds);
        return this;
    }

    /**
     * 删除功能权限
     *
     * @param actionIds 应用id
     */
    public Role removeAction(Long... actionIds) {
        this.removeAction(Arrays.asList(actionIds));
        return this;
    }

    public Role removeAction(List<Long> actionIds) {
        if (this.rolePermissions == null) {
            this.rolePermissions = RolePermissions.of(this.roleCode);
        }
        this.rolePermissions.removeActions(actionIds);
        return this;
    }

    @Override
    protected UaaRole buildEntity() {
        UaaRole entity = RoleRepository.getByCode(this.roleCode.get());
        if (entity == null) {
            entity = new UaaRole();
            entity.setCode(this.roleCode.get());
            entity.setName(this.name);
        }
        this.name = entity.getName();
        return entity;
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
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#deleteByCode}
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRole}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleAppMapper#deleteByRole}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMenuMapper#deleteByRole}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleActionMapper#deleteByRole}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleCode", this.roleCode.get());

        // delete role
        MybatisUtils.executeDelete(UaaRole.class, "deleteByCode", param);
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
        // delete roleApp
        MybatisUtils.executeDelete(UaaRoleApp.class, "deleteByRole", param);
        MybatisUtils.executeDelete(UaaRoleMenu.class, "deleteByRole", param);
        MybatisUtils.executeDelete(UaaRoleAction.class, "deleteByRole", param);
    }

    private Role(String roleCode) {
        this.roleCode = RoleId.of(roleCode);
    }

}
