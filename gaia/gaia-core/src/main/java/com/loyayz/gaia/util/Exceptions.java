package com.loyayz.gaia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * 异常断言工具类
 *
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exceptions {

    /**
     * 将异常转换为 RuntimeException
     */
    public static RuntimeException runtime(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    public static void isTrue(boolean expression, RuntimeException exception) {
        if (!expression) {
            throw exception;
        }
    }

    public static void isFalse(boolean expression, RuntimeException exception) {
        if (expression) {
            throw exception;
        }
    }

    public static void isNull(Object object, RuntimeException exception) {
        if (object != null) {
            throw exception;
        }
    }

    public static void notNull(Object object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        }
    }

    public static void hasLength(String text, RuntimeException exception) {
        if (!CommonUtils.hasLength(text)) {
            throw exception;
        }
    }

    public static void hasText(String text, RuntimeException exception) {
        if (!CommonUtils.hasText(text)) {
            throw exception;
        }
    }

    public static void notContain(String textToSearch, String substring, RuntimeException exception) {
        if (CommonUtils.hasLength(textToSearch) && CommonUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw exception;
        }
    }

    public static void isContain(String textToSearch, String substring, RuntimeException exception) {
        boolean contain = CommonUtils.hasLength(textToSearch) && CommonUtils.hasLength(substring) && textToSearch.contains(substring);
        if (!contain) {
            throw exception;
        }
    }

    public static void notEmpty(Object[] array, RuntimeException exception) {
        if (CommonUtils.isEmpty(array)) {
            throw exception;
        }
    }

    public static void isEmpty(Object[] array, RuntimeException exception) {
        if (!CommonUtils.isEmpty(array)) {
            throw exception;
        }
    }

    public static void noNullElements(Object[] array, RuntimeException exception) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw exception;
                }
            }
        }
    }

    public static void notEmpty(Collection<?> collection, RuntimeException exception) {
        if (CommonUtils.isEmpty(collection)) {
            throw exception;
        }
    }

    public static void isEmpty(Collection<?> collection, RuntimeException exception) {
        if (!CommonUtils.isEmpty(collection)) {
            throw exception;
        }
    }

    public static void notEmpty(Map<?, ?> map, RuntimeException exception) {
        if (CommonUtils.isEmpty(map)) {
            throw exception;
        }
    }

    public static void isEmpty(Map<?, ?> map, RuntimeException exception) {
        if (!CommonUtils.isEmpty(map)) {
            throw exception;
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, RuntimeException exception) {
        notNull(type, exception);
        if (!type.isInstance(obj)) {
            throw exception;
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, RuntimeException exception) {
        notNull(superType, exception);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw exception;
        }
    }

}
