package com.loyayz.gaia.auth.core.identity.impl;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.identity.AuthIdentityCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class JwtAuthIdentityCache implements AuthIdentityCache {
    private final AuthCredentialsProperties credentialsProperties;

    @Override
    public Map<String, Object> get(String token) {
        try {
            Claims result = this.credentialsProperties.getJwt()
                    .parseToken(token);
            result.putIfAbsent("id", result.getId());
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String put(String identityId, Map<String, Object> identity) {
        Date now = new Date();
        Date expirationDate = this.credentialsProperties.getExpirationDate(now);
        return this.credentialsProperties.getJwt()
                .generateToken(identityId, identity, now, expirationDate);
    }

    @Override
    public void remove(String token) {

    }

}
