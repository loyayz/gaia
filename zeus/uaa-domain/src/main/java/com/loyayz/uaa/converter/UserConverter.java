package com.loyayz.uaa.converter;

import com.loyayz.gaia.util.JsonUtils;
import com.loyayz.uaa.data.entity.UaaRole;
import com.loyayz.uaa.data.entity.UaaUser;
import com.loyayz.uaa.data.entity.UaaUserAccount;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleRole;
import com.loyayz.uaa.dto.SimpleUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {

    public static UaaUser toEntity(SimpleUser user) {
        UaaUser result = new UaaUser();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setMobile(user.getMobile());
        result.setEmail(user.getEmail());
        result.setInfo(JsonUtils.write(user.getInfos()));
        result.setLocked(user.getLocked() ? 1 : 0);
        result.setDeleted(0);
        return result;
    }

    public static UaaRole toEntity(SimpleRole role) {
        UaaRole result = new UaaRole();
        result.setCode(role.getCode());
        result.setName(role.getName());
        return result;
    }

    public static SimpleUser toSimple(UaaUser user) {
        SimpleUser result = new SimpleUser();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setMobile(user.getMobile());
        result.setEmail(user.getEmail());
        result.setInfos(JsonUtils.read(user.getInfo()));
        result.setLocked(Integer.valueOf(1).equals(user.getLocked()));
        result.setCreateTime(user.getGmtCreate().getTime());
        return result;
    }

    public static UaaUserAccount toEntity(SimpleAccount account, Long userId) {
        UaaUserAccount result = new UaaUserAccount();
        result.setUserId(userId);
        result.setType(account.getType());
        result.setName(account.getName());
        result.setPassword(account.getPassword());
        return result;
    }

    public static SimpleAccount toSimple(UaaUserAccount account) {
        SimpleAccount result = new SimpleAccount();
        result.setType(account.getType());
        result.setName(account.getName());
        result.setPassword(account.getPassword());
        result.setCreateTime(account.getGmtCreate().getTime());
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
