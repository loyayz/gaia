package com.loyayz.uaa.domain.user;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.domain.UserRepository;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.zeus.AbstractEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class User extends AbstractEntity<UaaUser> {
    private Long userId;
    private List<UserAccount> updatedAccounts = new ArrayList<>();
    private List<UserAccount> deletedAccounts = new ArrayList<>();
    private UserRoles userRoles;

    private SimpleUser user;

    public static User of() {
        return new User();
    }

    public static User of(Long userId) {
        return new User(userId);
    }

    public Long userId() {
        return super.entity().getId();
    }

    /**
     * 修改用户名
     */
    public User name(String name) {
        SimpleUser user = new SimpleUser();
        user.setName(name);
        this.fill(user);
        return this;
    }

    /**
     * 修改手机号
     */
    public User mobile(String mobile) {
        SimpleUser user = new SimpleUser();
        user.setMobile(mobile);
        this.fill(user);
        return this;
    }

    /**
     * 修改邮箱
     */
    public User email(String email) {
        SimpleUser user = new SimpleUser();
        user.setEmail(email);
        this.fill(user);
        return this;
    }

    /**
     * 修改用户详情
     * 有则修改，无则新增
     */
    public User info(Map<String, Object> infos) {
        SimpleUser user = new SimpleUser();
        user.setInfos(infos);
        this.fill(user);
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
     * 修改用户信息
     */
    public User fill(SimpleUser user) {
        if (this.user == null) {
            this.user = new SimpleUser();
        }
        this.user.fill(user);
        UserConverter.setEntity(super.entity(), this.user);
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
     * 添加账号
     */
    public User addAccount(String accountType, String accountName, String password) {
        UserAccount account = UserAccount.of(this.userId(), accountType, accountName, password);
        this.updatedAccounts = this.updatedAccounts.stream()
                .filter(a -> !a.same(accountType, accountName))
                .collect(Collectors.toList());
        this.updatedAccounts.add(account);
        return this;
    }

    /**
     * 删除账号
     */
    public User removeAccount(String accountType, String accountName) {
        UserAccount account = UserAccount.of(this.userId(), accountType, accountName);
        this.updatedAccounts = this.updatedAccounts.stream()
                .filter(a -> !a.same(accountType, accountName))
                .collect(Collectors.toList());
        this.deletedAccounts.add(account);
        return this;
    }

    /**
     * 修改账号密码
     */
    public User changeAccountPassword(String accountType, String accountName, String password) {
        UserAccount account = UserAccount.of(this.userId(), accountType, accountName, password);
        Optional<UserAccount> accountOptional = this.updatedAccounts.stream()
                .filter(a -> a.same(accountType, accountName))
                .findFirst();
        if (accountOptional.isPresent()) {
            accountOptional.get().changePassword(password);
        } else {
            this.updatedAccounts.add(account);
        }
        return this;
    }

    /**
     * 添加角色
     */
    public User addRole(String... roleCodes) {
        this.addRole(Arrays.asList(roleCodes));
        return this;
    }

    public User addRole(List<String> roleCodes) {
        if (userRoles == null) {
            userRoles = UserRoles.of(this.userId());
        }
        userRoles.addCodes(roleCodes);
        return this;
    }

    /**
     * 删除角色
     */
    public User removeRole(String... roleCodes) {
        this.removeRole(Arrays.asList(roleCodes));
        return this;
    }

    public User removeRole(List<String> roleCodes) {
        if (userRoles == null) {
            userRoles = UserRoles.of(this.userId());
        }
        userRoles.removeCodes(roleCodes);
        return this;
    }

    @Override
    protected UaaUser buildEntity() {
        if (this.userId == null) {
            return UserConverter.toEntity(this.user);
        } else {
            return UserRepository.findById(this.userId);
        }
    }

    @Override
    public void save() {
        super.save();
        this.userId = super.entity().getId();

        for (UserAccount account : updatedAccounts) {
            account.save();
        }
        for (UserAccount account : deletedAccounts) {
            account.delete();
        }
        if (userRoles != null) {
            userRoles.save();
        }
    }

    /**
     * 删除用户
     * 逻辑删除用户信息，物理删除账号和角色
     * {@link com.loyayz.uaa.data.mapper.UaaUserAccountMapper#deleteByUser}
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#deleteByUser}
     */
    @Override
    public void delete() {
        // delete user
        UaaUser user = new UaaUser();
        user.setId(this.userId());
        user.setDeleted(1);
        user.updateById();

        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", this.userId());
        // delete account
        MybatisUtils.executeDelete(UaaUserAccount.class, "deleteByUser", param);
        // delete userRole
        MybatisUtils.executeDelete(UaaUserRole.class, "deleteByUser", param);
    }

    private User() {
    }

    private User(Long userId) {
        this.userId = userId;
    }

}
