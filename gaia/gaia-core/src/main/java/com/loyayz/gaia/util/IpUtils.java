package com.loyayz.gaia.util;

import javax.servlet.http.HttpServletRequest;


/**
 * ip 工具类
 * 使用时请先加 servlet 依赖
 *
 * @author loyayz (loyayz@foxmail.com)
 */
public final class IpUtils {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_FOR_SPLIT_SYMBOL = ",";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String UNKNOWN = "unknown";

    /**
     * 获取请求的真实 ip
     * 1. header X-Forwarded-For
     * 2. header X-Real-IP
     * 3. {@link HttpServletRequest#getRemoteAddr()}
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String result = request.getHeader(X_FORWARDED_FOR);
        if (hasIp(result)) {
            result = result.contains(X_FORWARDED_FOR_SPLIT_SYMBOL) ?
                    result.split(X_FORWARDED_FOR_SPLIT_SYMBOL)[0].trim() : result;
        }
        if (hasIp(result)) {
            return result;
        }
        result = request.getHeader(X_REAL_IP);
        return hasIp(result) ? result : request.getRemoteAddr();
    }

    private static boolean hasIp(String ip) {
        return CommonUtils.hasText(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }

}
