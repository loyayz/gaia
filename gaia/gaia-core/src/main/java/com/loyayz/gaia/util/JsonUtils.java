package com.loyayz.gaia.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class JsonUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    @SneakyThrows
    public static String write(Object object) {
        if (object == null) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    @SneakyThrows
    public static Map<String, Object> read(String json) {
        if (json == null) {
            return Collections.emptyMap();
        }
        return (Map<String, Object>) OBJECT_MAPPER.readValue(json, Map.class);
    }

    @SneakyThrows
    public static <T> T read(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return OBJECT_MAPPER.readValue(json, clazz);
    }

}
