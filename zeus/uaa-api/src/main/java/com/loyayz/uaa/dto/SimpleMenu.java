package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleMenu implements Serializable {
    private static final long serialVersionUID = -1L;

    private String parentCode;
    /**
     * 编码
     */
    private String code;
    /**
     * 菜单名
     */
    private String name;
    /**
     * 链接
     */
    private String url;
    /**
     * 图标
     */
    private String icon;
    /**
     * 备注
     */
    private String remark;
    /**
     * 序号
     */
    private Integer sort;

    public Integer getSort() {
        return sort == null ? 0 : sort;
    }

}
