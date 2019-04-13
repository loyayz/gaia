package com.loyayz.gaia.exception.core;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface ExceptionDefiner {

    /**
     * 要处理的异常
     */
    List<Class<? extends Throwable>> exceptions();

    /**
     * 业务异常编码
     */
    String code(Throwable e);

    /**
     * 业务异常信息
     */
    default String message(Throwable e) {
        return e.getMessage();
    }

    /**
     * http 状态码
     */
    default int status(Throwable e) {
        return 501;
    }

    /**
     * 异常等级
     */
    default int level(Throwable e) {
        return 1;
    }

    default ExceptionResult getResult(Throwable exception) {
        ExceptionResult result = new ExceptionResult();
        result.setStatus(this.status(exception));
        result.setCode(this.code(exception));
        result.setMessage(this.message(exception));
        return result;
    }

}
