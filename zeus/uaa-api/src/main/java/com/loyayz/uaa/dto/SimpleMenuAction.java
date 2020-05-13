package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleMenuAction implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 菜单
     */
    private String menuCode;
    /**
     * 编码
     */
    private String code;
    /**
     * 菜单名
     */
    private String name;
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
