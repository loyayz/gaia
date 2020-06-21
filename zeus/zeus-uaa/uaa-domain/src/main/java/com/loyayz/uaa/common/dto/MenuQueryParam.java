package com.loyayz.uaa.common.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class MenuQueryParam {

    private Long appId;
    private Long pid;
    /**
     * 菜单编码
     * 模糊查询
     */
    private String code;
    /**
     * 菜单名
     * 模糊查询
     */
    private String name;
    /**
     * 链接
     * 模糊查询
     */
    private String url;

}
