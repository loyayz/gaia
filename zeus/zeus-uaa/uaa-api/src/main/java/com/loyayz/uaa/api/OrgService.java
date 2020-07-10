package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.OrgQueryParam;
import com.loyayz.uaa.dto.SimpleOrg;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface OrgService {

    /**
     * 根据id查询组织
     */
    default SimpleOrg getOrg(Long orgId) {
        return this.getOrgTree(orgId, 0);
    }

    /**
     * 根据id查询组织和下级组织
     *
     * @param orgId    组织 id
     * @param itemDeep 往下查几级
     * @return 组织树
     */
    SimpleOrg getOrgTree(Long orgId, int itemDeep);

    /**
     * 查下级组织
     */
    List<SimpleOrg> listOrgByPid(Long pid);

    /**
     * 分页查询组织
     */
    PageModel<SimpleOrg> pageOrg(OrgQueryParam queryParam, PageRequest pageRequest);

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
