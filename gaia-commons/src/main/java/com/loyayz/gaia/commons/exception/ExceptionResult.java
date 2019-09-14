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

}
