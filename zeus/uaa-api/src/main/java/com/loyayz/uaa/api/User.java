package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleUser;

import java.util.Arrays;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface User {

    /**
     * 删除用户
     * 逻辑删除用户信息，物理删除账号和角色
     */
    void delete();

    /**
     * 修改用户
     *
     * @param user 用户信息
     */
    void update(SimpleUser user);

    /**
     * 修改用户名
     */
    default void updateName(String name) {
        SimpleUser user = new SimpleUser();
        user.setName(name);
        this.update(user);
    }

    /**
     * 修改手机号
     */
    default void updateMobile(String mobile) {
        SimpleUser user = new SimpleUser();
        user.setMobile(mobile);
        this.update(user);
    }

    /**
     * 修改邮箱
     */
    default void updateEmail(String email) {
        SimpleUser user = new SimpleUser();
        user.setEmail(email);
        this.update(user);
    }

    /**
     * 修改用户详情
     * 有则修改，无则新增
     */
    void updateInfo(String key, Object value);

    /**
     * 添加账号
     *
     * @param account 账号信息
     */
    void addAccount(SimpleAccount account);

    /**
     * 删除账号
     *
     * @param account 账号信息
     */
    void deleteAccount(SimpleAccount account);

    /**
     * 修改密码
     *
     * @param account 账号信息
     */
    void updateAccountPassword(SimpleAccount account);

    /**
     * 添加角色
     *
     * @param roleCodes 角色编码
     */
    default void addRole(String... roleCodes) {
        this.addRole(Arrays.asList(roleCodes));
    }

    /**
     * 添加角色
     *
     * @param roleCodes 角色编码
     */
    void addRole(List<String> roleCodes);

    /**
     * 删除角色
     *
     * @param roleCodes 角色编码
     */
    default void deleteRole(String... roleCodes) {
        this.deleteRole(Arrays.asList(roleCodes));
    }

    /**
     * 删除角色
     *
     * @param roleCodes 角色编码
     */
    void deleteRole(List<String> roleCodes);

}
