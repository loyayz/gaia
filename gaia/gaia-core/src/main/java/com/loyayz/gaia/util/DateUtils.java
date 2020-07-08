package com.loyayz.gaia.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class DateUtils {

    private static ZoneId timezone = ZoneId.systemDefault();

    /**
     * 当前时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(timezone);
    }

    /**
     * 当前时间戳
     *
     * @param pattern 时间戳格式。 如： yyyy-MM-dd
     * @return 当前时间戳
     */
    public static String now(String pattern) {
        return format(now(), pattern);
    }

    /**
     * 格式化时间戳
     *
     * @param date    日期
     * @param pattern 时间戳格式。 如： yyyy-MM-dd
     * @return 时间戳
     */
    public static String format(Date date, String pattern) {
        return format(date.getTime(), pattern);
    }

    /**
     * 格式化时间戳
     *
     * @param milliseconds 毫秒。自 1970-01-01T00:00:00Z
     * @param pattern      时间戳格式。如： yyyy-MM-dd
     * @return 时间戳
     */
    public static String format(Long milliseconds, String pattern) {
        LocalDateTime dateTime = Instant.ofEpochMilli(milliseconds)
                .atZone(timezone)
                .toLocalDateTime();
        return format(dateTime, pattern);
    }

    /**
     * 格式化时间戳
     *
     * @param date    日期
     * @param pattern 时间戳格式。 如： yyyy-MM-dd
     * @return 时间戳
     */
    public static String format(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(timezone);
        return date.format(formatter);
    }

    /**
     * 转为时间对象
     *
     * @param date   时间戳
     * @param format 时间戳格式
     * @return 时间
     */
    public static LocalDateTime parse(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(timezone);
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * 转为 时间对象
     */
    public static LocalDateTime parse(Date date) {
        return parse(date.toInstant());
    }

    /**
     * 转为 时间对象
     */
    public static LocalDateTime parse(Instant date) {
        return date.atZone(timezone).toLocalDateTime();
    }

    /**
     * 转为 date
     *
     * @param dateStr 时间戳
     * @param format  时间戳格式
     */
    public static Date date(String dateStr, String format) {
        LocalDateTime dateTime = parse(dateStr, format);
        return date(dateTime);
    }

    /**
     * 转为 date
     */
    public static Date date(LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(timezone).toInstant();
        return date(instant);
    }

    /**
     * 转为 date
     */
    public static Date date(Instant dateTime) {
        return Date.from(dateTime);
    }

    public static ZoneId timeZone() {
        return timezone;
    }

    public static void setTimeZone(ZoneId zone) {
        DateUtils.timezone = zone;
    }

}
