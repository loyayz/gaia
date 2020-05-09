package com.loyayz.gaia.auth.core.identity.impl;

import com.loyayz.gaia.auth.core.identity.AuthIdentity;
import com.loyayz.gaia.auth.core.identity.AuthIdentityCacheDataConverter;
import com.loyayz.gaia.auth.core.identity.AuthIdentityRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class DefaultAuthIdentityCacheDataConverter implements AuthIdentityCacheDataConverter {

    @Override
    public Map<String, Object> toCacheData(AuthIdentity identity) {
        Map<String, Object> result = new HashMap<>(8);
        result.put("id", identity.getId());
        result.put("type", identity.getType());
        result.put("name", identity.getName());
        result.put("roles", this.transRoles(identity.getRoles()));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AuthIdentity toIdentity(Map<String, Object> identity) {
        if (identity == null) {
            return null;
        }
        List<AuthIdentityRole> roles = this.parseRoles((List) identity.get("roles"));
        return AuthIdentity.builder()
                .id((String) identity.get("id"))
                .type((String) identity.get("type"))
                .name((String) identity.get("name"))
                .roles(roles)
                .build();
    }

    private List<Map<String, Object>> transRoles(List<AuthIdentityRole> roles) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (roles != null) {
            for (AuthIdentityRole role : roles) {
                Map<String, Object> item = new HashMap<>(4);
                item.put("code", role.getCode());
                item.put("name", role.getName());
                result.add(item);
            }
        }
        return result;
    }

    private List<AuthIdentityRole> parseRoles(List<Map<String, Object>> roles) {
        List<AuthIdentityRole> result = new ArrayList<>();
        if (roles != null) {
            for (Map<String, Object> role : roles) {
                result.add(
                        AuthIdentityRole.builder()
                                .code((String) role.get("code"))
                                .name((String) role.get("name"))
                                .build()
                );
            }
        }
        return result;
    }

}
