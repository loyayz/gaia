package com.loyayz.uaa.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleApp implements Serializable {
    private static final long serialVersionUID = -1L;


    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 远程组件
     */
    private Boolean remote;
    /**
     * 地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 创建日期
     */
    private Long createTime;

}
