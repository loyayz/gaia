package com.loyayz.uaa.domain.role;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRole;
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
    private final RoleId roleCode;
    private String name;
    private RoleUsers roleUsers;

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
        if (roleUsers == null) {
            roleUsers = RoleUsers.of(this.roleCode);
        }
        roleUsers.addUsers(userIds);
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
        if (roleUsers == null) {
            roleUsers = RoleUsers.of(this.roleCode);
        }
        roleUsers.removeUsers(userIds);
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
        if (roleUsers != null) {
            roleUsers.save();
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#deleteByCode}
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRole}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("roleCode", this.roleCode.get());

        // delete role
        MybatisUtils.executeDelete(UaaRole.class, "deleteByCode", param);
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
    }

    private Role(String roleCode) {
        this.roleCode = RoleId.of(roleCode);
    }

}
