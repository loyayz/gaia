package com.loyayz.gaia.auth.core.credentials;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
        String token = this.getHeaderToken(request).orElse(
                this.getParamToken(request).orElse("")
        );
        return new AuthCredentials(token);
    }

    private Optional<String> getHeaderToken(T request) {
        String headerName = this.credentialsProperties.getTokenHeaderName();
        if (headerName == null || headerName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getHeaderToken(request, headerName));
    }

    private Optional<String> getParamToken(T request) {
        String paramName = this.credentialsProperties.getTokenParamName();
        if (paramName == null || paramName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getParamToken(request, paramName));
    }

}
