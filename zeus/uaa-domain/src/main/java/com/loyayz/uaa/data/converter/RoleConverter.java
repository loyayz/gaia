package com.loyayz.uaa.data.converter;

import com.loyayz.uaa.common.dto.SimpleRole;
import com.loyayz.uaa.data.UaaAppRole;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class RoleConverter {

    public static SimpleRole toSimple(UaaAppRole role) {
        SimpleRole result = new SimpleRole();
        result.setId(role.getId());
        result.setName(role.getName());
        result.setCreateTime(role.getGmtCreate().getTime());
        return result;
    }

}
