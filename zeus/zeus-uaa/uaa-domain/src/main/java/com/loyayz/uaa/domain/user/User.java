package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.EntityId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class User extends AbstractEntity<UaaUser, Long> {
    private final UserRoles userRoles;
    private final UserOrgs userOrgs;

    public static User of() {
        return of(null);
    }

    public static User of(Long userId) {
        return new User(userId);
    }

    /**
     * 修改用户名
     */
    public User name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    /**
     * 修改手机号
     */
    public User mobile(String mobile) {
        super.entity().setMobile(mobile);
        super.markUpdated();
        return this;
    }

    /**
     * 修改邮箱
     */
    public User email(String email) {
        super.entity().setEmail(email);
        super.markUpdated();
        return this;
    }

    /**
     * 修改用户详情
     * 有则修改，无则新增
     */
    public User info(Map<String, Object> infos) {
        if (infos != null) {
            Map<String, Object> infoMap = UserConverter.infoMap(super.entity().getInfo());
            infoMap.putAll(infos);
            super.entity().setInfo(UserConverter.infoStr(infoMap));
            super.markUpdated();
        }
        return this;
    }

    /**
     * 修改用户详情
     * 有则修改，无则新增
     */
    public User addInfo(String key, Object value) {
        Map<String, Object> infos = new HashMap<>(2);
        infos.put(key, value);
        this.info(infos);
        return this;
    }

    /**
     * 删除用户详情
     */
    public User removeInfo(String... keys) {
        Map<String, Object> infos = UserConverter.infoMap(super.entity().getInfo());
        for (String key : keys) {
            infos.remove(key);
        }
        super.entity().setInfo(UserConverter.infoStr(infos));
        super.markUpdated();
        return this;
    }

    /**
     * 注销（锁住用户信息）
     */
    public User lock() {
        super.entity().setLocked(1);
        super.markUpdated();
        return this;
    }

    /**
     * 解锁
     */
    public User unlock() {
        super.entity().setLocked(0);
        super.markUpdated();
        return this;
    }

    /**
     * 账号
     *
     * @param accountType 账号类型
     * @param accountName 账号名
     */
    public UserAccount account(String accountType, String accountName) {
        return UserAccount.of(super.id(), accountType, accountName);
    }

    /**
     * 添加角色
     *
     * @param roleIds 角色id
     */
    public User addRole(List<Long> roleIds) {
        this.userRoles.add(roleIds);
        return this;
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id
     */
    public User removeRole(List<Long> roleIds) {
        this.userRoles.remove(roleIds);
        return this;
    }

    /**
     * 添加组织
     *
     * @param orgIds 组织id
     */
    public User addOrg(List<Long> orgIds) {
        this.userOrgs.add(orgIds);
        return this;
    }

    /**
     * 删除角色
     *
     * @param orgIds 组织id
     */
    public User removeOrg(List<Long> orgIds) {
        this.userOrgs.remove(orgIds);
        return this;
    }

    @Override
    protected UaaUser buildEntity() {
        if (super.hasId()) {
            return UserRepository.findById(super.idValue());
        } else {
            UaaUser entity = new UaaUser();
            entity.setLocked(0);
            entity.setDeleted(0);
            return entity;
        }
    }

    @Override
    protected void saveExtra() {
        this.userRoles.save();
        this.userOrgs.save();
    }

    /**
     * 删除用户
     * 逻辑删除用户信息，物理删除账号、角色、应用管理员
     * {@link com.loyayz.uaa.data.mapper.UaaUserAccountMapper#deleteByUser}
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUser}
     */
    @Override
    public void delete() {
        Long userId = super.idValue();
        // delete user
        UaaUser user = new UaaUser();
        user.setId(userId);
        user.setDeleted(1);
        user.updateById();

        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", userId);
        // delete account
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteByUser", param);
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUser", param);
    }

    private User(Long userId) {
        super(userId);
        EntityId id = super.id();
        this.userRoles = UserRoles.of(id);
        this.userOrgs = UserOrgs.of(id);
    }

}
