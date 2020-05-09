package com.loyayz.gaia.auth.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class AuthCredentialsProperties {

    private static final String DEFAULT_TOKEN_HEADER_NAME = "Authorization";
    private static final String DEFAULT_TOKEN_PARAM_NAME = "_token";

    /**
     * token 放在哪个 header
     */
    private String tokenHeaderName = DEFAULT_TOKEN_HEADER_NAME;
    /**
     * token 放在哪个 url 参数
     * 当 header 没找到时，查 url 参数
     */
    private String tokenParamName = DEFAULT_TOKEN_PARAM_NAME;

    /**
     * token 的有效期
     */
    private Duration ttl = Duration.ofHours(12);
    /**
     * jwt 配置
     */
    private Jwt jwt = new Jwt();

    public Date getExpirationDate(Date now) {
        long currentTime = now.getTime();
        long ttlTime = this.getTtl().toMillis();
        long expMillis = currentTime + ttlTime;
        return new Date(expMillis);
    }

    @Data
    public static class Jwt {
        private static final SignatureAlgorithm DEFAULT_ALGORITHM = SignatureAlgorithm.HS256;

        /**
         * 密钥
         */
        private String secret;
        /**
         * 算法
         */
        private String algorithm;

        public Key getSignSecret() {
            String secret = this.getSecret();
            byte[] encodedKey = Base64.getDecoder().decode(secret);
            return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        }

        public SignatureAlgorithm getSignAlgorithm() {
            String algorithm = this.getAlgorithm();
            if (algorithm == null || algorithm.isEmpty()) {
                return DEFAULT_ALGORITHM;
            }
            return SignatureAlgorithm.forName(algorithm);
        }

        public String generateToken(String id, Map<String, Object> claims, Date now, Date expirationDate) {
            return Jwts.builder()
                    .setClaims(claims)
                    .setId(id)
                    .setSubject("user")
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(this.getSignAlgorithm(), this.getSignSecret())
                    .compact();
        }

        public Claims parseToken(String token) {
            return Jwts.parser()
                    .setSigningKey(this.getSignSecret())
                    .parseClaimsJws(token)
                    .getBody();
        }

    }

}
