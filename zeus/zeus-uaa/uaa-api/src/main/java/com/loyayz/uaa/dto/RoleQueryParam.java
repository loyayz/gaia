package com.loyayz.uaa.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class RoleQueryParam {

    /**
     * 应用
     */
    private Long appId;
    /**
     * 角色名
     * 模糊查询
     */
    private String name;

}
