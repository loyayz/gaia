package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.*;
import com.loyayz.uaa.dto.UserQueryParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class UserRepository {

    public static UaaUser findById(Long userId) {
        return new UaaUser().findById(userId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserMapper#listByParam}
     */
    public static List<UaaUser> listUserByParam(UserQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaUser.class, "listByParam", queryParam);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#listByUser}
     */
    public static List<UaaRole> listRoleByUser(Long userId) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", userId);
        return MybatisUtils.executeSelectList(UaaUserRole.class, "listByUser", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserAccountMapper#getAccount}
     */
    public static UaaUserAccount getAccount(String type, String name) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("type", type);
        param.put("name", name);
        return MybatisUtils.executeSelectOne(UaaUserAccount.class, "getAccount", param);
    }

    public static List<UaaUserAccount> listAccount(Long userId) {
        return UaaUserAccount.builder().userId(userId).build()
                .listByCondition();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserRoleMapper#listRoleByUserRoles}
     */
    public static List<Long> listRoleIds(Long userId, Collection<Long> roleIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("userId", userId);
        param.put("roleIds", roleIds);
        return MybatisUtils.executeSelectList(UaaUserRole.class, "listRoleByUserRoles", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaOrgUserMapper#listOrgByUserOrgs}
     */
    public static List<Long> listOrgIds(Long userId, Collection<Long> orgIds) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("userId", userId);
        param.put("orgIds", orgIds);
        return MybatisUtils.executeSelectList(UaaOrgUser.class, "listOrgByUserOrgs", param);
    }

}
