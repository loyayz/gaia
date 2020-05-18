package com.loyayz.uaa.data.converter;

import com.loyayz.gaia.util.Functions;
import com.loyayz.gaia.util.JsonUtils;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.data.UaaUserAccount;
import com.loyayz.uaa.dto.SimpleAccount;
import com.loyayz.uaa.dto.SimpleUser;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class UserConverter {

    public static SimpleUser toSimple(UaaUser user) {
        SimpleUser result = new SimpleUser();
        result.setId(user.getId());
        result.setName(user.getName());
        result.setMobile(user.getMobile());
        result.setEmail(user.getEmail());
        result.setInfos(infoMap(user.getInfo()));
        result.setLocked(Integer.valueOf(1).equals(user.getLocked()));
        result.setCreateTime(user.getGmtCreate().getTime());
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

    public static Map<String, Object> infoMap(String infos) {
        return JsonUtils.read(infos);
    }

    public static String infoStr(Map<String, Object> infos) {
        return JsonUtils.write(infos);
    }

}
