package com.loyayz.gaia.commons.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Assert {

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

    public static void isNull(@Nullable Object object, RuntimeException exception) {
        if (object != null) {
            throw exception;
        }
    }

    public static void notNull(@Nullable Object object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        }
    }

    public static void hasLength(@Nullable String text, RuntimeException exception) {
        if (!StringUtils.hasLength(text)) {
            throw exception;
        }
    }

    public static void hasText(@Nullable String text, RuntimeException exception) {
        if (!StringUtils.hasText(text)) {
            throw exception;
        }
    }

    public static void doesNotContain(@Nullable String textToSearch, String substring, RuntimeException exception) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw exception;
        }
    }

    public static void isContain(@Nullable String textToSearch, String substring, RuntimeException exception) {
        boolean contain = StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring);
        if (!contain) {
            throw exception;
        }
    }

    public static void notEmpty(@Nullable Object[] array, RuntimeException exception) {
        if (ObjectUtils.isEmpty(array)) {
            throw exception;
        }
    }

    public static void isEmpty(@Nullable Object[] array, RuntimeException exception) {
        if (!ObjectUtils.isEmpty(array)) {
            throw exception;
        }
    }

    public static void noNullElements(@Nullable Object[] array, RuntimeException exception) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw exception;
                }
            }
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, RuntimeException exception) {
        if (CollectionUtils.isEmpty(collection)) {
            throw exception;
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, RuntimeException exception) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw exception;
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, RuntimeException exception) {
        if (CollectionUtils.isEmpty(map)) {
            throw exception;
        }
    }

    public static void isEmpty(@Nullable Map<?, ?> map, RuntimeException exception) {
        if (!CollectionUtils.isEmpty(map)) {
            throw exception;
        }
    }

    public static void isInstanceOf(Class<?> type, @Nullable Object obj, RuntimeException exception) {
        notNull(type, exception);
        if (!type.isInstance(obj)) {
            throw exception;
        }
    }

    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, RuntimeException exception) {
        notNull(superType, exception);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw exception;
        }
    }

    private Assert() {

    }

}
