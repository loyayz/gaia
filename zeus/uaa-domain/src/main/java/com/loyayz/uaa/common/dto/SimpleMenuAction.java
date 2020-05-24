package com.loyayz.uaa.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleMenuAction implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;

}
