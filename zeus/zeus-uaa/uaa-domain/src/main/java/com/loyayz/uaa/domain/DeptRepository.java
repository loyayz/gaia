package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaDept;
import com.loyayz.uaa.data.UaaDeptRole;
import com.loyayz.uaa.data.UaaDeptUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class DeptRepository {

    public static UaaDept findById(Long deptId) {
        return new UaaDept().findById(deptId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptMapper#getMaxSortByParent}
     */
    public static Integer getNextSort(Long parentId) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("pid", parentId);
        Integer sort = MybatisUtils.executeSelectOne(UaaDept.class, "getMaxSortByParent", param);
        return sort == null ? 0 : sort + 1;
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptRoleMapper#listRoleByDeptRoles}
     */
    public static List<Long> listRoleIds(Long roleId, Collection<Long> roleIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("deptId", roleId);
        param.put("roleIds", roleIds);
        return MybatisUtils.executeSelectList(UaaDeptRole.class, "listRoleByDeptRoles", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaDeptUserMapper#listUserByDeptUsers}
     */
    public static List<Long> listUserIds(Long roleId, Collection<Long> userIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("deptId", roleId);
        param.put("userIds", userIds);
        return MybatisUtils.executeSelectList(UaaDeptUser.class, "listUserByDeptUsers", param);
    }

}
