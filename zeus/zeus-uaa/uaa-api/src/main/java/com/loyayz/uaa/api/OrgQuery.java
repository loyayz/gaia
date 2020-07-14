package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.OrgQueryParam;
import com.loyayz.uaa.dto.SimpleOrg;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface OrgQuery {

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

}
