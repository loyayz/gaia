package com.loyayz.gaia.auth.core.user.strategy;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AuthCredentials;
import com.loyayz.gaia.auth.core.user.AuthUser;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public class JwtAuthUserCache implements AuthUserCache {
    private final AuthCredentialsConfiguration authCredentialsConfiguration;

    @Override
    @SuppressWarnings("unchecked")
    public AuthUser getUserFromCache(AuthCredentials credentials) {
        Claims claims;
        try {
            claims = this.parseToken(credentials.getToken());
        } catch (Exception e) {
            return null;
        }
        return AuthUser.builder()
                .id(claims.getId())
                .name(claims.get("name", String.class))
                .roles(claims.get("roles", List.class))
                .build();
    }

    @Override
    public AuthCredentials putUserInCache(AuthUser user) {
        Map<String, Object> claims = new HashMap<>(8);
        claims.put("name", user.getName());
        claims.put("roles", user.getRoles());
        String userId = user.getId();
        String token = this.generateToken(userId, claims);
        return new AuthCredentials(token);
    }

    @Override
    public void removeUserFromCache(AuthCredentials credentials) {

    }

    private String generateToken(String id, Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = this.authCredentialsConfiguration.getExpirationDate(now);
        AuthCredentialsConfiguration.Jwt jwt = this.authCredentialsConfiguration.getJwt();
        return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setSubject("user")
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(jwt.getSignAlgorithm(), jwt.getSignSecret())
                .compact();
    }

    private Claims parseToken(String token) {
        AuthCredentialsConfiguration.Jwt jwt = this.authCredentialsConfiguration.getJwt();
        return Jwts.parser()
                .setSigningKey(jwt.getSignSecret())
                .parseClaimsJws(token)
                .getBody();
    }

}
