package com.loyayz.gaia.auth.core.user.strategy;

import com.loyayz.gaia.auth.core.SecurityCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AuthenticationCredentials;
import com.loyayz.gaia.auth.core.user.SecurityUser;
import com.loyayz.gaia.auth.core.user.SecurityUserCache;
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
public class JwtSecurityUserCache implements SecurityUserCache {
    private final SecurityCredentialsConfiguration securityCredentialsConfiguration;

    @Override
    @SuppressWarnings("unchecked")
    public SecurityUser getUserFromCache(AuthenticationCredentials credentials) {
        Claims claims;
        try {
            claims = this.parseToken(credentials.getToken());
        } catch (Exception e) {
            return null;
        }
        return SecurityUser.builder()
                .id(claims.getId())
                .name(claims.get("name", String.class))
                .roles(claims.get("roles", List.class))
                .build();
    }

    @Override
    public AuthenticationCredentials putUserInCache(SecurityUser user) {
        Map<String, Object> claims = new HashMap<>(8);
        claims.put("name", user.getName());
        claims.put("roles", user.getRoles());
        String userId = user.getId();
        String token = this.generateToken(userId, claims);
        return new AuthenticationCredentials(token);
    }

    @Override
    public void removeUserFromCache(String token) {

    }

    private String generateToken(String id, Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = this.securityCredentialsConfiguration.getExpirationDate(now);
        SecurityCredentialsConfiguration.Jwt jwt = this.securityCredentialsConfiguration.getJwt();
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
        SecurityCredentialsConfiguration.Jwt jwt = this.securityCredentialsConfiguration.getJwt();
        return Jwts.parser()
                .setSigningKey(jwt.getSignSecret())
                .parseClaimsJws(token)
                .getBody();
    }

}
