package com.loyayz.uaa.data.converter;

import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.dto.SimpleRole;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleConverter {

    public static UaaRole toEntity(SimpleRole role) {
        UaaRole result = new UaaRole();
        result.setCode(role.getCode());
        result.setName(role.getName());
        return result;
    }

    public static SimpleRole toSimple(UaaRole role) {
        SimpleRole result = new SimpleRole();
        result.setCode(role.getCode());
        result.setName(role.getName());
        result.setCreateTime(role.getGmtCreate().getTime());
        return result;
    }

}
