package com.loyayz.gaia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class NumberUtils {

    public static BigDecimal toBigDecimal(Object number) {
        return toBigDecimal(number, null);
    }

    public static BigDecimal toBigDecimal(Object number, BigDecimal defaultValue) {
        if (number == null) {
            return defaultValue;
        }
        try {
            return (number instanceof BigDecimal) ?
                    (BigDecimal) number : new BigDecimal(String.valueOf(number));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int toInt(Object number) {
        return toInt(number, 0);
    }

    public static int toInt(Object number, int defaultValue) {
        return toBigDecimal(number, new BigDecimal(defaultValue)).intValue();
    }

    public static long toLong(Object number) {
        return toLong(number, 0);
    }

    public static long toLong(Object number, int defaultValue) {
        return toBigDecimal(number, new BigDecimal(defaultValue)).longValue();
    }

    /**
     * 是否相等
     */
    public static boolean isEquals(Number n1, Number n2) {
        if (n1 != null && n2 != null) {
            return toBigDecimal(n1).compareTo(toBigDecimal(n2)) == 0;
        }
        return false;
    }

    /**
     * 加法
     */
    public static <T extends Number> BigDecimal add(T... datas) {
        BigDecimal result = BigDecimal.ZERO;
        for (Number data : datas) {
            result = result.add(toBigDecimal(data));
        }
        return result;
    }

    /**
     * 减法
     */
    public static <T extends Number> BigDecimal subtract(T beSub, T... datas) {
        BigDecimal result = toBigDecimal(beSub);
        for (Number data : datas) {
            result = result.subtract(toBigDecimal(data));
        }
        return result;
    }

    /**
     * 乘法
     */
    public static <T extends Number> BigDecimal multiply(T... datas) {
        BigDecimal result = BigDecimal.ONE;
        for (Number data : datas) {
            result = result.multiply(toBigDecimal(data));
        }
        return result;
    }

    /**
     * 除法
     * 默认四舍五入保留小数点后两位
     */
    public static <T extends Number> BigDecimal divide(T beDivisor, T divisor) {
        return divide(beDivisor, divisor, 2);
    }

    public static <T extends Number> BigDecimal divide(T beDivisor, T divisor, int scale) {
        return divide(beDivisor, divisor, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static <T extends Number> BigDecimal divide(T beDivisor, T divisor, int scale, int roundingMode) {
        if (null == beDivisor || null == divisor ||
                isEquals(beDivisor, BigDecimal.ZERO) || isEquals(divisor, BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return toBigDecimal(beDivisor).divide(toBigDecimal(divisor), scale, roundingMode);
    }

    /**
     * 平均数
     * 四舍五入保留两位小数
     */
    public static BigDecimal average(List<BigDecimal> nums) {
        return average(nums, 2);
    }

    public static BigDecimal average(List<BigDecimal> nums, int scale) {
        if (nums == null || nums.isEmpty()) {
            throw new IllegalArgumentException();
        }
        BigDecimal total = nums.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return divide(total, nums.size(), scale);
    }

    /**
     * 随机
     * 四舍五入保留两位小数
     *
     * @param min 最小值
     * @param max 最大值
     */
    public static BigDecimal random(String min, String max) {
        return random(min, max, 2);
    }

    /**
     * 随机
     *
     * @param min   最小值
     * @param max   最大值
     * @param scale 保留几位小数
     */
    public static BigDecimal random(String min, String max, int scale) {
        BigDecimal factor = BigDecimal.TEN.pow(scale);
        int randomMin = multiply(new BigDecimal(min), factor).intValue();
        int randomMax = multiply(new BigDecimal(max), factor).intValue();
        int diff = randomMax - randomMin + 1;
        int random = new Random().nextInt(diff) + randomMin;
        return divide(random, factor, scale);
    }

}
