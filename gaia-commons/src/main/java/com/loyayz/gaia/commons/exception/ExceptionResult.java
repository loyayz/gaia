package com.loyayz.gaia.commons.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class ExceptionResult implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * http 状态码
     */
    private Integer status;
    /**
     * 业务异常编码
     */
    private String code;
    /**
     * 业务异常信息
     */
    private String message;

    /**
     * 默认异常
     *
     * @param e 异常
     * @return 状态码：501
     */
    public static ExceptionResult defaultResponse(Throwable e) {
        ExceptionResult result = new ExceptionResult();
        result.setCode("501");
        result.setMessage(e.getMessage());
        result.setStatus(501);
        result.setStatus(1);
        return result;
    }

    public String toJsonString() {
        return "{" +
                "\"status\":" + this.getStatus() + "," +
                "\"code\":\"" + this.getCode() + "\"," +
                "\"message\":\"" + this.getMessage() + "\"" +
                "}";
    }

}
