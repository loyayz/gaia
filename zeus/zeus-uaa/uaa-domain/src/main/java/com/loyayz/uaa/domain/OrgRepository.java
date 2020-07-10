package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaOrg;
import com.loyayz.uaa.data.UaaOrgRole;
import com.loyayz.uaa.data.UaaOrgUser;
import com.loyayz.uaa.dto.OrgQueryParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class OrgRepository {

    public static UaaOrg findById(Long orgId) {
        return new UaaOrg().findById(orgId);
    }

    public static List<UaaOrg> listByPid(Long pid) {
        if (pid == null) {
            pid = -1L;
        }
        OrgQueryParam param = new OrgQueryParam();
        param.setPid(pid);
        return listByParam(param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaOrgMapper#listByParam}
     */
    public static List<UaaOrg> listByParam(OrgQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaOrg.class, "listByParam", queryParam);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaOrgMapper#getMaxSortByParent}
     */
    public static Integer getNextSort(Long pid) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("pid", pid);
        Integer sort = MybatisUtils.executeSelectOne(UaaOrg.class, "getMaxSortByParent", param);
        return sort == null ? 0 : sort + 1;
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaOrgRoleMapper#listRoleByOrgRoles}
     */
    public static List<Long> listRoleIds(Long roleId, Collection<Long> roleIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("orgId", roleId);
        param.put("roleIds", roleIds);
        return MybatisUtils.executeSelectList(UaaOrgRole.class, "listRoleByOrgRoles", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaOrgUserMapper#listUserByOrgUsers}
     */
    public static List<Long> listUserIds(Long roleId, Collection<Long> userIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("orgId", roleId);
        param.put("userIds", userIds);
        return MybatisUtils.executeSelectList(UaaOrgUser.class, "listUserByOrgUsers", param);
    }

}
