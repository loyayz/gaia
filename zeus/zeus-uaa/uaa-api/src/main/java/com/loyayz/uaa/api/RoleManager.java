package com.loyayz.uaa.api;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface RoleManager {

    /**
     * 新增角色
     *
     * @param appId    应用 id
     * @param roleName 角色名
     * @return 角色 id
     */
    Long addRole(Long appId, String roleName);

    /**
     * 修改角色
     *
     * @param roleId   角色 id
     * @param roleName 角色名
     */
    void updateRole(Long roleId, String roleName);

    /**
     * 删除角色
     */
    void deleteRole(List<Long> roleIds);

    /**
     * 角色添加用户
     */
    void addUser(Long roleId, List<Long> userIds);

    /**
     * 角色删除用户
     */
    void removeUser(Long roleId, List<Long> userIds);

    /**
     * 角色添加组织
     */
    void addOrg(Long roleId, List<Long> orgIds);

    /**
     * 角色删除组织
     */
    void removeOrg(Long roleId, List<Long> orgIds);

}
