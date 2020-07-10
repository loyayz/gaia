package com.loyayz.uaa.data.converter;

import com.loyayz.uaa.data.UaaOrg;
import com.loyayz.uaa.dto.SimpleOrg;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class OrgConverter {

    public static SimpleOrg toSimple(UaaOrg org) {
        SimpleOrg result = new SimpleOrg();
        result.setId(org.getId());
        result.setName(org.getName());
        result.setSort(org.getSort());
        result.setCreateTime(org.getGmtCreate().getTime());
        return result;
    }

}
