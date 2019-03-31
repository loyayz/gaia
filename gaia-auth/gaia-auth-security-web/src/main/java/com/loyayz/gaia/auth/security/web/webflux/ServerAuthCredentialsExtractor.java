package com.loyayz.gaia.auth.security.web.webflux;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.credentials.AbstractAuthCredentialsExtractor;
import com.loyayz.gaia.auth.core.credentials.AuthCredentialsExtractor;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class ServerAuthCredentialsExtractor extends AbstractAuthCredentialsExtractor<ServerWebExchange>
        implements AuthCredentialsExtractor<ServerWebExchange> {

    public ServerAuthCredentialsExtractor(AuthCredentialsConfiguration credentialsConfiguration) {
        super(credentialsConfiguration);
    }

    @Override
    protected String getHeaderToken(ServerWebExchange request, String headerName) {
        return request.getRequest().getHeaders().getFirst(headerName);
    }

    @Override
    protected String getParamToken(ServerWebExchange request, String paramName) {
        return request.getRequest().getQueryParams().getFirst(paramName);
    }

}
