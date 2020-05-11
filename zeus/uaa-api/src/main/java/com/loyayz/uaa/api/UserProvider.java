package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.*;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UserProvider {

    /**
     * 获取用户详情
     *
     * @param userId 用户 id
     */
    default UserInfo userInfo(Long userId) {
        return UserInfo.builder()
                .user(this.getUser(userId))
                .accounts(this.listUserAccount(userId))
                .roles(this.listUserRole(userId))
                .build();
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户 id
     */
    SimpleUser getUser(Long userId);

    /**
     * 账号列表
     *
     * @param userId 用户 id
     */
    List<SimpleAccount> listUserAccount(Long userId);

    /**
     * 用户角色列表
     *
     * @param userId 用户 id
     */
    List<SimpleRole> listUserRole(Long userId);

    /**
     * 查询用户信息列表
     *
     * @param queryParam  查询条件
     */
    List<SimpleUser> listUser(UserQueryParam queryParam);

    /**
     * 获取账号
     *
     * @param accountType 账号类型
     * @param accountName 账号名
     */
    SimpleAccount getAccount(String accountType, String accountName);

}
