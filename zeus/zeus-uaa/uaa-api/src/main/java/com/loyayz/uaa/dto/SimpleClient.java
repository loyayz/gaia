package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleClient implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 密钥
     */
    private ClientSecret secret;
    /**
     * 创建日期
     */
    private Long createTime;

}
