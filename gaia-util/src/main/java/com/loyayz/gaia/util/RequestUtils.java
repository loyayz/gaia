package com.loyayz.gaia.util;

import javax.servlet.http.HttpServletRequest;


/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class RequestUtils {

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
        if (!noIp(result)) {
            result = result.contains(X_FORWARDED_FOR_SPLIT_SYMBOL) ?
                    result.split(X_FORWARDED_FOR_SPLIT_SYMBOL)[0].trim() : result;
        }
        if (noIp(result)) {
            result = request.getHeader(X_REAL_IP);
        }
        return noIp(result) ? request.getRemoteAddr() : result;
    }

    private static boolean noIp(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equals(ip);
    }

}
