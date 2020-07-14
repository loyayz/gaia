package com.loyayz.uaa.helper;

import com.loyayz.uaa.dto.SimpleAccount;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UserAccountPasswordProvider {

    /**
     * 账号类型
     */
    String accountType();

    /**
     * 密码加密
     * 密码格式错误，抛异常 {@link com.loyayz.uaa.exception.AccountPasswordFormatException}
     */
    String encrypt(String password);

    /**
     * 校验密码
     *
     * @param account       账号信息
     * @param validPassword 要校验的密码
     * @return 是否一致
     */
    default boolean valid(SimpleAccount account, String validPassword) {
        if (account.getPassword() == null) {
            return false;
        }
        validPassword = this.encrypt(validPassword);
        return account.getPassword().equals(validPassword);
    }

    /**
     * 是否适用该账号类型
     */
    default boolean support(String accountType) {
        return this.accountType().equalsIgnoreCase(accountType);
    }

}
