package com.loyayz.gaia.auth.core.user.impl;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class JwtAuthUserCache implements AuthUserCache {
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
    public String put(String userId, Map<String, Object> user) {
        Date now = new Date();
        Date expirationDate = this.credentialsProperties.getExpirationDate(now);
        return this.credentialsProperties.getJwt()
                .generateToken(userId, user, now, expirationDate);
    }

    @Override
    public void remove(String token) {

    }

}
