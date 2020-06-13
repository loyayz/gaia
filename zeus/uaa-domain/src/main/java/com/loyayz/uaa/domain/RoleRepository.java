package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.RoleQueryParam;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.mapper.UaaRoleMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleRepository {

    public static UaaRole getRole(Long id) {
        return new UaaRole().findById(id);
    }

    public static List<Long> listRefIdByRole(Long roleId, String type) {
        return UaaRolePermission.builder().roleId(roleId).type(type).build()
                .listByCondition()
                .stream()
                .map(UaaRolePermission::getRefId)
                .collect(Collectors.toList());
    }

    /**
     * {@link UaaRoleMapper#listByParam}
     */
    public static List<UaaRole> listByParam(RoleQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaRole.class, "listByParam", queryParam);
    }

}
