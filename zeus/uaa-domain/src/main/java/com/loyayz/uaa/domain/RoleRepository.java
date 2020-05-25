package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.RoleQueryParam;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaRoleApp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleRepository {

    public static UaaRole getByCode(String code) {
        UaaRole role = new UaaRole();
        role.setCode(code);
        return role.listByCondition()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public static List<Long> listAppIdByRole(String roleCode) {
        return UaaRoleApp.builder().roleCode(roleCode).build()
                .listByCondition()
                .stream()
                .map(UaaRoleApp::getAppId)
                .collect(Collectors.toList());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#listByParam}
     */
    public static List<UaaRole> listByParam(RoleQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaRole.class, "listByParam", queryParam);
    }

}
