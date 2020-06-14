package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class User extends AbstractEntity<UaaUser> {
    private final UserId userId;

    public static User of() {
        return new User();
    }

    public static User of(Long userId) {
        return new User(userId);
    }

    public Long id() {
        return this.userId.get();
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
        super.entity().setInfo(UserConverter.infoStr(infos));
        super.markUpdated();
        return this;
    }

    /**
     * 修改用户详情
     * 有则修改，无则新增
     */
    public User addInfo(String key, Object value) {
        Map<String, Object> infos = UserConverter.infoMap(super.entity().getInfo());
        infos.put(key, value);
        this.info(infos);
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
        return UserAccount.of(this.userId, accountType, accountName);
    }

    @Override
    protected UaaUser buildEntity() {
        if (this.userId.isEmpty()) {
            UaaUser user = new UaaUser();
            user.setLocked(0);
            user.setDeleted(0);
            return user;
        } else {
            return UserRepository.findById(this.userId.get());
        }
    }

    @Override
    public void save() {
        super.save();
        this.userId.set(super.entity().getId());
    }

    /**
     * 删除用户
     * 逻辑删除用户信息，物理删除账号、角色、应用管理员
     * {@link com.loyayz.uaa.data.mapper.UaaUserAccountMapper#deleteByUser}
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUser}
     */
    @Override
    public void delete() {
        // delete user
        UaaUser user = new UaaUser();
        user.setId(this.userId.get());
        user.setDeleted(1);
        user.updateById();

        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId.get());
        // delete account
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteByUser", param);
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUser", param);
    }

    private User() {
        this.userId = UserId.of();
    }

    private User(Long userId) {
        this.userId = UserId.of(userId);
    }

}
