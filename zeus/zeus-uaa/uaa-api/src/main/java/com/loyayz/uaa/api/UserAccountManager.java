package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.SimpleAccount;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UserAccountManager {

    /**
     * 用户添加账号
     * 账号已存在，抛异常 {@link com.loyayz.uaa.exception.AccountExistException}
     */
    default void addAccount(Long userId, String accountType, String accountName) {
        this.addAccount(userId, accountType, accountName, "");
    }

    /**
     * 用户添加账号
     * 账号已存在，抛异常 {@link com.loyayz.uaa.exception.AccountExistException}
     */
    void addAccount(Long userId, String accountType, String accountName, String password);

    /**
     * 用户删除账号
     * 非本人账号，抛异常 {@link com.loyayz.gaia.exception.OperationDeniedException}
     */
    void removeAccount(Long userId, String accountType, String accountName);

    /**
     * 用户修改账号密码
     * 非本人账号，抛异常 {@link com.loyayz.gaia.exception.OperationDeniedException}
     * 账号密码错误，抛异常 {@link com.loyayz.uaa.exception.AccountPasswordIncorrectException}
     *
     * @param userId        用户 id
     * @param account       账号信息
     * @param validPassword 要校验的密码
     */
    void changePassword(Long userId, SimpleAccount account, String validPassword);

    /**
     * 用户重置账号密码
     * 非本人账号，抛异常 {@link com.loyayz.gaia.exception.OperationDeniedException}
     *
     * @param userId  用户 id
     * @param account 账号信息
     */
    void resetPassword(Long userId, SimpleAccount account);

}
