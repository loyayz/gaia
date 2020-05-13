package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.converter.RoleConverter;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;
import com.loyayz.uaa.exception.RoleExistException;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleRepository {

    public static Long insertRole(SimpleRole role) {
        UaaRole entity = RoleConverter.toEntity(role);
        MybatisUtils.saveThrowDuplicate(
                entity::insert,
                () -> {
                    throw new RoleExistException(role.getCode());
                }
        );
        return entity.getId();
    }

    public static UaaRole getByCode(String code) {
        UaaRole role = new UaaRole();
        role.setCode(code);
        return role.listByCondition()
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#listByParam}
     */
    public static List<UaaRole> listByParam(RoleQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaRole.class, "listByParam", queryParam);
    }

}
