package com.loyayz.uaa.common.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class RoleQueryParam {

    /**
     * 角色编码
     * 模糊查询
     */
    private String code;
    /**
     * 角色名
     * 模糊查询
     */
    private String name;

}
