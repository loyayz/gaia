package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.SimpleOrg;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface OrgManager {

    /**
     * 新增组织
     *
     * @param pid 上级组织
     * @param org 组织信息
     * @return 组织 id
     */
    Long addOrg(Long pid, SimpleOrg org);

    /**
     * 修改组织
     */
    void updateOrg(Long orgId, SimpleOrg org);

    /**
     * 删除组织
     */
    void deleteOrg(List<Long> orgIds);

    /**
     * 添加组织角色
     */
    void addRole(Long orgId, List<Long> roleIds);

    /**
     * 删除组织角色
     */
    void removeRole(Long orgId, List<Long> roleIds);

    /**
     * 添加组织用户
     */
    void addUser(Long orgId, List<Long> userIds);

    /**
     * 删除组织用户
     */
    void removeUser(Long orgId, List<Long> userIds);

}
