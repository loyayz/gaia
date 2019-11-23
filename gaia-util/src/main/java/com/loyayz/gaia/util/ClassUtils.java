package com.loyayz.gaia.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class ClassUtils {

    /**
     * 获取泛型的类型
     */
    public static <T> Class<T> resolveGenericType(Class<?> declaredClass) {
        ParameterizedType parameterizedType = (ParameterizedType) declaredClass.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

}
