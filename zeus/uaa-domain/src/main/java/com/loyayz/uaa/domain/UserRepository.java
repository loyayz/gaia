package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.converter.UserConverter;
import com.loyayz.uaa.data.entity.UaaRole;
import com.loyayz.uaa.data.entity.UaaUser;
import com.loyayz.uaa.data.entity.UaaUserAccount;
import com.loyayz.uaa.data.entity.UaaUserRole;
import com.loyayz.uaa.data.mapper.UaaUserAccountMapper;
import com.loyayz.uaa.data.mapper.UaaUserRoleMapper;
import com.loyayz.uaa.dto.SimpleUser;
import com.loyayz.uaa.dto.UserQueryParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class UserRepository {

    public static Long insertUser(SimpleUser user) {
        UaaUser entity = UserConverter.toEntity(user);
        entity.insert();
        return entity.getId();
    }

    public static UaaUser findById(Long userId) {
        return new UaaUser().findById(userId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaUserMapper#listByParam}
     */
    public static List<UaaUser> listUserByParam(UserQueryParam queryParam) {
        return MybatisUtils.executeSelectList(UaaUser.class, "listByParam", queryParam);
    }

    public static List<String> listRoleCodeByUser(Long userId) {
        return UaaUserRole.builder().userId(userId).build()
                .listByCondition()
                .stream()
                .map(UaaUserRole::getRoleCode)
                .collect(Collectors.toList());
    }

    public static List<Long> listUserByRole(String roleCode) {
        return UaaUserRole.builder().roleCode(roleCode).build()
                .listByCondition()
                .stream()
                .map(UaaUserRole::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * {@link UaaUserRoleMapper#listByUser}
     */
    public static List<UaaRole> listRoleByUser(Long userId) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", userId);
        return MybatisUtils.executeSelectList(UaaUserRole.class, "listByUser", param);
    }

    /**
     * {@link UaaUserAccountMapper#getAccount}
     */
    public static UaaUserAccount getAccount(String type, String name) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("type", type);
        param.put("name", name);
        return MybatisUtils.executeSelectOne(UaaUserAccount.class, "getAccount", param);
    }

}
