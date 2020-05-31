package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaAppRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.domain.RoleRepository;
import com.loyayz.zeus.AbstractEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AppRole extends AbstractEntity<UaaAppRole> {
    private final Long roleId;
    private AppRoleUsers roleUsers;

    static AppRole of(Long id) {
        return new AppRole(id);
    }

    public AppRole name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    /**
     * 添加用户
     *
     * @param userIds 用户id
     */
    public AppRole addUser(Long... userIds) {
        return this.addUser(Arrays.asList(userIds));
    }

    public AppRole addUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = AppRoleUsers.of(this.roleId);
        }
        this.roleUsers.addUsers(userIds);
        return this;
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     */
    public AppRole removeUser(Long... userIds) {
        return this.removeUser(Arrays.asList(userIds));
    }

    public AppRole removeUser(List<Long> userIds) {
        if (this.roleUsers == null) {
            this.roleUsers = AppRoleUsers.of(this.roleId);
        }
        this.roleUsers.removeUsers(userIds);
        return this;
    }

    @Override
    protected UaaAppRole buildEntity() {
        return RoleRepository.getRole(this.roleId);
    }

    @Override
    public void save() {
        super.save();

        if (this.roleUsers != null) {
            this.roleUsers.save();
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByRole}
     * {@link com.loyayz.uaa.data.mapper.UaaRolePermissionMapper#deleteByRole}
     */
    @Override
    public void delete() {
        new UaaAppRole().deleteById(this.roleId);

        Map<String, Object> param = new HashMap<>(2);
        param.put("roleId", this.roleId);
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByRole", param);
        // delete rolePermission
        MybatisUtils.executeDelete(UaaRolePermission.class, "deleteByRole", param);
    }

}
