package com.loyayz.gaia.auth.core.authentication;

import com.google.common.base.Splitter;
import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public abstract class AbstractAuthCredentialsExtractor<T> implements AuthCredentialsExtractor<T> {
    @Getter(AccessLevel.PROTECTED)
    private final AuthCredentialsProperties credentialsProperties;

    /**
     * 从 header 提取
     *
     * @param request    请求
     * @param headerName token 存放在的 header 名
     * @return token
     */
    protected abstract String getHeaderToken(T request, String headerName);

    /**
     * header 为空时，再从 url param 提取
     *
     * @param request   请求
     * @param paramName token 存放在的参数名
     * @return token
     */
    protected abstract String getParamToken(T request, String paramName);

    @Override
    public AuthCredentials extract(T request) {
        String token = this.extractToken(request);
        return this.buildCredentials(request, token);
    }

    protected String extractToken(T request) {
        return this.getHeaderToken(request).orElse(
                this.getParamToken(request).orElse("")
        );
    }

    protected AuthCredentials buildCredentials(T request, String token) {
        return new AuthCredentials(token);
    }

    private Optional<String> getHeaderToken(T request) {
        String headerName = this.credentialsProperties.getTokenHeaderName();
        if (headerName == null || headerName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getHeaderToken(request, headerName))
                .map(this::parseToken);
    }

    private Optional<String> getParamToken(T request) {
        String paramName = this.credentialsProperties.getTokenParamName();
        if (paramName == null || paramName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getParamToken(request, paramName))
                .map(this::parseToken);
    }

    private String parseToken(String token) {
        if (token == null) {
            return "";
        }
        String separator = " ";
        List<String> tokens = Splitter.on(separator).omitEmptyStrings().trimResults().splitToList(token);
        return tokens.size() > 1 ? tokens.get(1) : token;
    }

}
