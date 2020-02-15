package com.loyayz.gaia.auth.core.user.impl;

import com.loyayz.gaia.auth.core.user.AuthUser;
import com.loyayz.gaia.auth.core.user.AuthUserCacheItemConverter;
import com.loyayz.gaia.auth.core.user.AuthUserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class DefaultAuthUserCacheItemConverter implements AuthUserCacheItemConverter {

    @Override
    public Map<String, Object> toCacheItem(AuthUser user) {
        Map<String, Object> result = new HashMap<>(8);
        result.put("id", user.getId());
        result.put("name", user.getName());
        result.put("roles", this.transRoles(user.getRoles()));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AuthUser toUser(Map<String, Object> user) {
        if (user == null) {
            return null;
        }
        List<AuthUserRole> userRoles = this.parseRoles((List) user.get("roles"));
        return AuthUser.builder()
                .id((String) user.get("id"))
                .name((String) user.get("name"))
                .roles(userRoles)
                .build();
    }

    private List<Map<String, Object>> transRoles(List<AuthUserRole> roles) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (roles != null) {
            for (AuthUserRole role : roles) {
                Map<String, Object> item = new HashMap<>(4);
                item.put("code", role.getCode());
                item.put("name", role.getName());
                result.add(item);
            }
        }
        return result;
    }

    private List<AuthUserRole> parseRoles(List<Map<String, Object>> roles) {
        List<AuthUserRole> result = new ArrayList<>();
        if (roles != null) {
            for (Map<String, Object> role : roles) {
                result.add(
                        AuthUserRole.builder()
                                .code((String) role.get("code"))
                                .name((String) role.get("name"))
                                .build()
                );
            }
        }
        return result;
    }

}
