package com.loyayz.gaia.auth.core;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SecurityCredentialsConfiguration {

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


}
