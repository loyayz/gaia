package com.loyayz.gaia.auth.core.credentials;

import com.loyayz.gaia.auth.core.SecurityCredentialsConfiguration;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@RequiredArgsConstructor
public abstract class AbstractSecurityAuthCredentialsExtractor<T> implements SecurityAuthCredentialsExtractor<T> {
    private final SecurityCredentialsConfiguration securityCredentialsConfiguration;

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
    public AuthenticationCredentials extract(T request) {
        String token = this.getHeaderToken(request).orElse(
                this.getParamToken(request).orElse("")
        );
        return new AuthenticationCredentials(token);
    }

    private Optional<String> getHeaderToken(T request) {
        String headerName = this.securityCredentialsConfiguration.getTokenHeaderName();
        if (headerName == null || headerName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getHeaderToken(request, headerName));
    }

    private Optional<String> getParamToken(T request) {
        String paramName = this.securityCredentialsConfiguration.getTokenParamName();
        if (paramName == null || paramName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getParamToken(request, paramName));
    }

}
