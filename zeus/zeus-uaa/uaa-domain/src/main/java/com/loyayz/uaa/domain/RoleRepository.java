package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.UaaUserRole;
import com.loyayz.uaa.dto.RoleQueryParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleRepository {

    public static UaaRole getRole(Long id) {
        return new UaaRole().findById(id);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#listUserByRoleUsers}
     */
    public static List<Long> listUserIdByRoleUsers(Long roleId, Collection<Long> userIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("roleId", roleId);
        param.put("userIds", userIds);
        return MybatisUtils.executeSelectList(UaaUserRole.class, "listUserByRoleUsers", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRolePermissionMapper#listRefByRoleTypeRefs}
     */
    public static List<Long> listPermissionRefIdByRoleRefs(Long roleId, String type, List<Long> refIds) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("roleId", roleId);
        param.put("type", type);
        param.put("refIds", refIds);
        return MybatisUtils.executeSelectList(UaaRolePermission.class, "listRefByRoleTypeRefs", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#listByParam}
     */
    public static List<UaaRole> listByParam(RoleQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaRole.class, "listByParam", queryParam);
    }

}
