package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.EntityId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Role extends AbstractEntity<UaaRole, Long> {
    private final RoleUsers roleUsers;
    private final RoleOrgs roleOrgs;
    private final RolePermissions rolePermissions;

    public static Role of() {
        return new Role(null);
    }

    public static Role of(Long id) {
        return new Role(id);
    }

    public Role appId(Long appId) {
        super.entity().setAppId(appId);
        super.markUpdated();
        return this;
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
        this.roleUsers.add(userIds);
        return this;
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     */
    public Role removeUser(List<Long> userIds) {
        this.roleUsers.remove(userIds);
        return this;
    }

    /**
     * 添加组织
     *
     * @param orgIds 组织id
     */
    public Role addOrg(List<Long> orgIds) {
        this.roleOrgs.add(orgIds);
        return this;
    }

    /**
     * 删除组织
     *
     * @param orgIds 组织id
     */
    public Role removeOrg(List<Long> orgIds) {
        this.roleOrgs.remove(orgIds);
        return this;
    }

    public Role addPermission(BasePermission permission) {
        this.rolePermissions.add(permission);
        return this;
    }

    public Role removePermission(BasePermission permission) {
        this.rolePermissions.remove(permission);
        return this;
    }

    @Override
    protected UaaRole buildEntity() {
        if (super.hasId()) {
            return RoleRepository.findById(super.idValue());
        } else {
            return new UaaRole();
        }
    }

    @Override
    protected void saveExtra() {
        this.roleUsers.save();
        this.roleOrgs.save();
        this.rolePermissions.save();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRole}
     * {@link com.loyayz.uaa.data.mapper.UaaRolePermissionMapper#deleteByRole}
     */
    @Override
    protected void deleteExtra() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleId", super.idValue());
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
        // delete rolePermission
        MybatisUtils.executeDelete(UaaRolePermission.class, "deleteByRole", param);
    }

    private Role(Long roleId) {
        super(roleId);
        EntityId id = super.id();
        this.roleUsers = RoleUsers.of(id);
        this.roleOrgs = RoleOrgs.of(id);
        this.rolePermissions = RolePermissions.of(id);
    }
}
