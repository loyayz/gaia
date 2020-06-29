package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleRole implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 角色
     */
    private Long id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 创建日期
     */
    private Long createTime;

}
