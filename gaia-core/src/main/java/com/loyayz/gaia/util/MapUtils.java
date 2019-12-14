package com.loyayz.gaia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(Map<String, Object> map, String key, T defaultValue) {
        Object val = map.getOrDefault(key, defaultValue);
        if (val == null) {
            return defaultValue;
        }
        if (defaultValue instanceof String) {
            val = val.toString();
        }
        return (T) val;
    }

}
