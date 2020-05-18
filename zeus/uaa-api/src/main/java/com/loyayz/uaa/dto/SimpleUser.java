package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleUser implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 详情
     */
    private Map<String, Object> infos;
    /**
     * 是否锁定
     */
    private Boolean locked;
    /**
     * 创建日期
     */
    private Long createTime;

}
