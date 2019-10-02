package com.loyayz.gaia.exception;

import com.loyayz.gaia.exception.ExceptionDisposer;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@FunctionalInterface
public interface ExceptionLoggerStrategy {

    /**
     * 异常日志
     *
     * @param apiMethod 接口方法名
     * @param apiUrl    接口链接
     * @param exception 接口抛出的异常
     * @param disposer  异常处理器
     */
    void write(String apiMethod, String apiUrl, Throwable exception, ExceptionDisposer disposer);

}
