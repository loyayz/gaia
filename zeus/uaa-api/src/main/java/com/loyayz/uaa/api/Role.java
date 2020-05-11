package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.SimpleRole;

import java.util.Arrays;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface Role {

    /**
     * 删除角色
     */
    void delete();

    /**
     * 修改角色
     *
     * @param role 角色信息
     */
    void update(SimpleRole role);

    /**
     * 添加用户
     *
     * @param userIds 用户id
     */
    default void addUser(Long... userIds) {
        this.addUser(Arrays.asList(userIds));
    }

    /**
     * 添加用户
     *
     * @param userIds 用户id
     */
    void addUser(List<Long> userIds);

    /**
     * 删除用户
     *
     * @param userIds 用户id
     */
    default void deleteUser(Long... userIds) {
        this.deleteUser(Arrays.asList(userIds));
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     */
    void deleteUser(List<Long> userIds);

}
