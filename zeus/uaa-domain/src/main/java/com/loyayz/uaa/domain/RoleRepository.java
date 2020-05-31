package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.RoleQueryParam;
import com.loyayz.uaa.data.UaaAppRole;
import com.loyayz.uaa.data.UaaRolePermission;
import com.loyayz.uaa.data.mapper.UaaAppRoleMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleRepository {

    public static UaaAppRole getRole(Long id) {
        return new UaaAppRole().findById(id);
    }

    public static List<Long> listRefIdByRole(Long roleId, String type) {
        return UaaRolePermission.builder().roleId(roleId).type(type).build()
                .listByCondition()
                .stream()
                .map(UaaRolePermission::getRefId)
                .collect(Collectors.toList());
    }

    /**
     * {@link UaaAppRoleMapper#listByParam}
     */
    public static List<UaaAppRole> listByParam(RoleQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaAppRole.class, "listByParam", queryParam);
    }

}
